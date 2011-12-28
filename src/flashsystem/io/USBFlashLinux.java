package flashsystem.io;

import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import java.io.IOException;

import win32lib.JKernel32;
import linuxlib.JUsb;

public class USBFlashLinux {
	
	private static int lastflags;
	private static byte[] lastreply;
	
	public static void open() throws IOException {
		try {
			JUsb.openDevice();
			readReply();
			if (lastreply == null) throw new IOException("Unable to read from device");
			
		}catch (Exception e) {
			if (lastreply == null) throw new IOException("Unable to read from device");
		}
	}

	public static void write(S1Packet p) throws IOException,X10FlashException {
		JUsb.writeDevice(p);
		int count = 0;
		while (true) {
			try {
				readReply();
				break;
			}
			catch (IOException e) {
				try {
					Thread.sleep(500);
				}
				catch (Exception s) {}
				count++;
				if (count==3) {
					throw e;
				}
			}
			catch (X10FlashException e) {
				throw e;
			}
		}
	}

    private static  void readReply() throws X10FlashException, IOException
    {
    	S1Packet p = JUsb.readDevice();
    	if (p!=null) {
    		lastreply = p.getDataArray();
    		lastflags = p.getFlags();
    	}
    	else {
    		lastreply = null;
    	}
    	p.release();
    }

    public static int getLastFlags() {
    	return lastflags;
    }
    
    public static byte[] getLastReply() {
    	return lastreply;
    }

    public static void close() {
		JUsb.closeDevice();
	}

}