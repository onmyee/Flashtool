package linuxlib;

import java.io.IOException;
import java.util.Iterator;
import java.util.HashSet;

import org.logger.MyLogger;

import flashsystem.BytesUtil;
import flashsystem.HexDump;
import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import se.marell.libusb.LibUsbPermissionException;
import se.marell.libusb.LibUsbSystem;
import se.marell.libusb.UsbDevice;
import se.marell.libusb.UsbSystem;

public class JUsb {
	
	static final byte[] data1 = new byte[65536];
	static UsbSystem system = new LibUsbSystem(false, 0);
	static UsbDevice device = null;
	
	public static Iterator<UsbDevice> getDeviceList(UsbSystem us) throws IOException {
		try {
			Iterator<UsbDevice> i = system.visitUsbDevices(new ListDevices()).iterator();
			return i;
		}
		catch (Exception e) {
			throw new IOException("Cannot get list of USB devices");
		}
	}

	public static HashSet<LinuxUsbDevice> getConnectedDevices() {
		HashSet<LinuxUsbDevice> result = new HashSet<LinuxUsbDevice>();
		try {
	    	Iterator<UsbDevice> i = system.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    		if (vendor.equals("0FCE")) {
	    			device=d;
	    			result.add(new LinuxUsbDevice(vendor,product));
	    	    }
	    	}
		}
    	catch (Exception e) {
    	}
		return result;
	}

	public static void openDevice() {
		try {
			device.open();
			if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			device.claim_interface(0);
		}
		catch (LibUsbPermissionException pe) {
			MyLogger.getLogger().error("Missing permissions on USB device");
			MyLogger.getLogger().error("Set this udev rule : SUBSYSTEM==\"usb\", ACTION==\"add\", SYSFS{idVendor}==\"0fce\", SYSFS{idProduct}==\"adde\", MODE=\"0777\"");
		}
		catch (Exception e) {
			MyLogger.getLogger().error(e.getMessage());
		}
	}

	public static void closeDevice() {
		try {
			device.release_interface(0);
			device.close();
			device=null;
		}
		catch (Exception e) {
		}
	}
	
	  public static S1Packet readDevice() throws IOException,X10FlashException {
		  S1Packet p=null;
			  boolean finished = false;
			  int read1=0;
			  try {
				  read1 = device.bulk_read(0x81, data1, 0);
			  }
			  catch (Exception e) {
				  throw new IOException("readReply : First data read request failed");
			  }
			  if (read1>0) {
				  p = new S1Packet(BytesUtil.getReply(data1,read1));
				  finished=!p.hasMoreToRead();
				  try {
					  while (true) {
						  if (finished)
							  read1 = device.bulk_read(0x81, data1, 20);
						  else {
							  read1 = device.bulk_read(0x81, data1, 0);
							  if (read1 > 0) {
								  p.addData(BytesUtil.getReply(data1,read1));
								  finished=!p.hasMoreToRead();
							  }
						  }
					  }
				  }
				  catch (Exception e) {
				  }
			  }
		  p.validate();
		  return p;
	  }

	  public static void writeDevice(S1Packet p) throws IOException {
		  try {
			  device.bulk_write(0x01, p.getByteArray(), 500);
		  }
		  catch (Exception e) {
			  throw new IOException(e.getMessage());
		  }
	  }

	  public static void clean() {
		  try {
			  system.cleanup();
		  }
		  catch (Exception e) {}
	  }
}