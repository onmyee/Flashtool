package linuxlib;

import java.util.Iterator;

import flashsystem.HexDump;
import flashsystem.S1Packet;

import se.marell.libusb.LibUsbSystem;
import se.marell.libusb.UsbDevice;
import se.marell.libusb.UsbSystem;

public class JUsb {

	public static S1Packet read() {
		S1Packet p = null;
		try {
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    	    if (vendor.equals("0FCE") && product.equals("ADDE")) {
	    	    	System.out.println("reading");
	    	    	p = readDevice(d);
	    	  	  	break;
	    	    }
	    	}
	    	us.cleanup();
	    	return p;
		}
    	catch (Exception e) {
    		return p;
    	}
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
	
	public static void write(S1Packet p) {
		try {
			UsbSystem us = new LibUsbSystem(false, 0);;
	    	Iterator<UsbDevice> i = us.visitUsbDevices(new ListDevices()).iterator();
	    	while (i.hasNext()) {
	    		UsbDevice d = i.next();
	    		String vendor = HexDump.toHex(d.getIdVendor());
	    		String product = HexDump.toHex(d.getIdProduct());
	    		if (vendor.equals("0FCE") && product.equals("ADDE")) {
	    	    	writeDevice(d,p);
	    	  	  	break;
	    	    }
	    	}
	    	us.cleanup();
		}
    	catch (Exception e) {
    	}
	}

	  public static S1Packet readDevice(UsbDevice device) {
		  try {
			  device.open();
			  S1Packet p=null;
			  boolean finished = false;
			  if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			  device.claim_interface(0);
			  while (!finished) {
				  byte[] data1 = new byte[65536];
				  int read1 = device.bulk_read(0x81, data1, 0);
				  System.out.println("Number of read bytes : "+read1);
				  if (read1 == 4) {
					  p.addData(getReply(data1,read1));
					  finished=!p.hasMoreToRead();
				  }
				  else {
					  p = new S1Packet(getReply(data1,read1));
					  finished=!p.hasMoreToRead();
				  }
			  }
			  device.release_interface(0);
			  device.close();
			  return p;
		  }
		  catch (Exception e) {
			  return null;
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

	  public static void writeDevice(UsbDevice device, S1Packet p) {
		  try {
			  device.open();
			  if (device.kernel_driver_active(0)) device.detach_kernel_driver(0);
			  device.claim_interface(0);
			  device.bulk_write(0x01, p.getByteArray(), 500);
			  device.release_interface(0);
			  device.close();
		  }
		  catch (Exception e) {
			  System.out.println("Error writing packet");
			  System.out.println(e.getMessage());
		  }
	  }

}