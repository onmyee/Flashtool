package flashsystem.io;

import flashsystem.S1Packet;
import flashsystem.X10FlashException;
import java.io.IOException;
import org.system.Device;
import org.system.DeviceChangedListener;
import org.system.OS;

public class USBFlash {

	public static void open() throws IOException {
		if (Device.getLastConnected().getPid().equals("ADDE"))
			DeviceChangedListener.pause(true);
		if (OS.getName().equals("windows")) {
			USBFlashWin32.open();
		}
		else {
			USBFlashLinux.open();
		}
	}

	public static void write(S1Packet p) throws IOException,X10FlashException {
		if (OS.getName().equals("windows")) {
			USBFlashWin32.write(p);
		}
		else {
			USBFlashLinux.write(p);
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