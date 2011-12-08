package flashsystem.io;

import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import java.io.IOException;
import org.system.OS;

public class USBFlash {

	static S1Packet lastreply;
	
	public static void open() throws IOException {
		if (OS.getName().equals("windows")) {
			USBFlashWin32.open();
		}
		else {
			USBFlashLinux.open();
		}
	}

	public static boolean write(S1Packet p) throws IOException,X10FlashException {
		if (OS.getName().equals("windows")) {
			USBFlashWin32.write(p);
		}
		else {
			USBFlashLinux.write(p);
		}
		return true;
	}

	public static void readReply() throws IOException, X10FlashException {
		if (OS.getName().equals("windows")) {
			USBFlashWin32.readReply();
		}
		else {
			USBFlashLinux.readReply();
		}
	}
	
   public static int getLastFlags() {
		if (OS.getName().equals("windows")) {
			return USBFlashWin32.getLastFlags();
		}
		else {
			return USBFlashLinux.getLastFlags();
		}
    }

    public static byte[] getLastReply() {
		if (OS.getName().equals("windows")) {
			return USBFlashWin32.getLastReply();
		}
		else {
			return USBFlashLinux.getLastReply();
		}
    }

}