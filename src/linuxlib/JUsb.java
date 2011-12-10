package linuxlib;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

import flashsystem.HexDump;
import flashsystem.S1Packet;

import se.marell.libusb.LibUsbSystem;
import se.marell.libusb.UsbDevice;
import se.marell.libusb.UsbSystem;

public class JUsb {
	
	static final byte[] data1 = new byte[65536];
	
	public static S1Packet read() throws IOException {
		S1Packet p = null;
		boolean found = false;
		try {
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
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
	    	if (!found) throw new IOException("device not connected");
	    	p.validate();
	    	return p;
		}
    	catch (Exception e) {
    		throw new IOException(e.getMessage());
    	}
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

	  public static S1Packet readDevice(UsbDevice device) throws IOException {
		  try {
			  device.open();
			  S1Packet p=null;
			  boolean finished = false;
			  if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			  device.claim_interface(0);
			  int read1 = device.bulk_read(0x81, data1, 0);
			  if (read1>0) {
				  p = new S1Packet(getReply(data1,read1));
				  finished=!p.hasMoreToRead();
				  try {
					  while (true) {
						  if (finished)
							  read1 = device.bulk_read(0x81, data1, 20);
						  else {
							  read1 = device.bulk_read(0x81, data1, 0);
							  if (read1 > 0) {
								  p.addData(getReply(data1,read1));
								  finished=!p.hasMoreToRead();
							  }
						  }
					  }
				  }
				  catch (Exception e) {
				  }
			  }
			  device.release_interface(0);
			  device.close();
			  return p;
		  }
		  catch (Exception e) {
			  throw new IOException(e.getMessage());
		  }
	  }

	  private static byte[] getReply(byte[] reply, int nbread) {
			byte[] newreply=null;
			if (nbread > 0) {
				newreply = new byte[nbread];
				System.arraycopy(reply, 0, newreply, 0, nbread);
			}
			return newreply;
	  }

	  public static void writeDevice(UsbDevice device, S1Packet p) throws IOException {
		  try {
			  device.open();
			  if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			  device.claim_interface(0);
			  byte[] towrite = p.getByteArray();
			  device.bulk_write(0x01, towrite, 500);
			  towrite=null;
			  device.release_interface(0);
			  device.close();
		  }
		  catch (Exception e) {
			  throw new IOException(e.getMessage());
		  }
	  }

}