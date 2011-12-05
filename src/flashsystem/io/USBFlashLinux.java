package flashsystem.io;

import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import java.io.IOException;

import linuxlib.JUsb;

public class USBFlashLinux {
	
	private static int lastflags;
	private static byte[] lastreply;
	
	public static void open() throws IOException {
		try {
			readReply();
		}catch (Exception e) {
		}
	}
	
	public static boolean write(S1Packet p) throws IOException,X10FlashException {
		JUsb.write(p);
		readReply();
		return true;
	}

    public static  void readReply() throws X10FlashException, IOException
    {
    	S1Packet p = JUsb.read();
		System.out.println("Read "+p.getCommand()+" with a data lenght of "+p.getDataLength());
		lastreply = p.getDataArray();
		lastflags = p.getFlags();
    }

    public static int getLastFlags() {
    	return lastflags;
    }
    
    public static byte[] getLastReply() {
    	return lastreply;
    }

}