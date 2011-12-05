package flashsystem.io;

import com.sonyericsson.cs.usbflashnative.impl.USBFlashNativeInterface;
import com.sonyericsson.cs.usbflashnative.impl.USBFlashNativeImpl;
import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import java.io.IOException;
import org.system.OS;

public class USBFlashJNI {
	
	private static USBFlashNativeInterface usbflash = new USBFlashNativeImpl();
	private static int channel=-1;
	private static int lastflags;
	private static byte[] lastreply;
	
	public static int openChannel(String paramString) throws IOException {
		channel = usbflash.openChannel(paramString, false);
		try {
			readReply();
		}catch (Exception e) {
		}
		return channel;
	}
	
	public static boolean close() throws IOException {
		boolean result = usbflash.close(channel);
		channel = -1;
		return result;
	}

	public static boolean writeBytes(byte[] paramArrayOfByte) throws IOException {
		return usbflash.writeBytes(channel, paramArrayOfByte);
	}

	public static byte[] readBytes(int bufferlen) throws IOException {
		return usbflash.readBytes(channel, bufferlen);
	}

    public static  void readReply() throws X10FlashException, IOException
    {
        byte b[] = readBytes(0x10000);
        S1Packet p = new S1Packet(b);
		do {
			b = readBytes(0x10000);
			p.addData(b);
		} while (p.hasMoreToRead());
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
    
	static {
		if (OS.getName().equals("windows")) System.loadLibrary("USBFlash");
	}

}