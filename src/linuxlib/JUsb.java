package linuxlib;

import java.io.IOException;
import java.util.Iterator;
import java.util.HashSet;

import org.logger.MyLogger;

import flashsystem.BytesUtil;
import flashsystem.HexDump;
import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import se.marell.libusb.LibUsbNoDeviceException;
import se.marell.libusb.LibUsbOtherException;
import se.marell.libusb.LibUsbPermissionException;
import se.marell.libusb.LibUsbSystem;
import se.marell.libusb.UsbDevice;
import se.marell.libusb.UsbSystem;

public class JUsb {
	
	static final byte[] data1 = new byte[65536];
	
	public static Iterator<UsbDevice> getDeviceList(UsbSystem us) throws IOException {
		try {
			Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
			return i;
		}
		catch (Exception e) {
			throw new IOException("Cannot get list of USB devices");
		}
	}

	public static S1Packet read() throws X10FlashException, IOException {
		S1Packet p = null;
		boolean found = false;
		UsbSystem us = new LibUsbSystem(false, 0);
    	Iterator<UsbDevice> i = getDeviceList(us);
    	while (i.hasNext()) {
    		UsbDevice d = i.next();
    		String vendor = HexDump.toHex(d.getIdVendor());
    		String product = HexDump.toHex(d.getIdProduct());
    	    if (vendor.equals("0FCE") && product.equals("ADDE")) {
    	    	p = readDevice(d);
    	    	found = true;
    	  	  	break;
    	    }
    	}
    	us.cleanup();
    	if (!found) throw new IOException("Device not connected");
		return p;
	}

	public static HashSet<LinuxUsbDevice> getConnectedDevices() {
		HashSet<LinuxUsbDevice> result = new HashSet<LinuxUsbDevice>();
		try {
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    		if (vendor.equals("0FCE")) {
	    			result.add(new LinuxUsbDevice(vendor,product));
	    	    }
	    	}
	    	us.cleanup();
		}
    	catch (Exception e) {
    	}
		return result;
	}
	
	public static String getConnectedPID() {
		String pid = "";
		try {
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    		if (vendor.equals("0FCE") && product.equals("ADDE")) {
	    			pid = product;
	    	  	  	break;
	    	    }
	    	}
	    	us.cleanup();
		}
    	catch (Exception e) {
    	}
		return pid;
	}
	
	public static void write(S1Packet p) throws IOException {
		try {
			boolean found = false;
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    		if (vendor.equals("0FCE") && product.equals("ADDE")) {
	    	    	writeDevice(d,p);
	    	    	found = true;
	    	  	  	break;
	    	    }
	    	}
	    	us.cleanup();
	    	if (!found) throw new IOException("device not connected");
		}
    	catch (Exception e) {
    		throw new IOException(e.getMessage());
    	}
	}

	public static boolean openDevice(UsbDevice device) {
		try {
			device.open();
			if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			device.claim_interface(0);
		  	return true;
		}
		catch (LibUsbPermissionException pe) {
			MyLogger.getLogger().error("Missing permissions on USB device");
			MyLogger.getLogger().error("Set this udev rule : SUBSYSTEM==\"usb\", ACTION==\"add\", SYSFS{idVendor}==\"0fce\", SYSFS{idProduct}==\"adde\", MODE=\"0777\"");
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public static void closeDevice(UsbDevice device) {
		try {
			device.release_interface(0);
			device.close();
		}
		catch (Exception e) {
		}
	}
	
	  public static S1Packet readDevice(UsbDevice device) throws IOException,X10FlashException {
		  S1Packet p=null;
		  if (!openDevice(device)) throw new IOException("readReply : Cannot open device");
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
		  closeDevice(device);
		  p.validate();
		  return p;
	  }

	  public static void writeDevice(UsbDevice device, S1Packet p) throws IOException {
		  try {
			  if (!openDevice(device)) throw new IOException("Cannot open device");
			  device.bulk_write(0x01, p.getByteArray(), 500);
			  closeDevice(device);
		  }
		  catch (Exception e) {
			  throw new IOException(e.getMessage());
		  }
	  }

}