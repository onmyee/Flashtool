package win32lib;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class JUSBFlash {
	
	static USBFlash usbflash = (USBFlash) Native.loadLibrary("USBFlash", USBFlash.class);

	public int openChannel(String paramString, boolean paramBoolean) {
		return usbflash.Java_com_sonyericsson_cs_subflashnative_impl_USBFlashNativeImpl_openChannel(paramString, paramBoolean);
	}

}