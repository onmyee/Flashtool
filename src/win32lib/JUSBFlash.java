package win32lib;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class JUSBFlash {
	
	static USBFlash usbflash = (USBFlash) Native.loadLibrary("USBFlash", USBFlash.class);

	public int openChannel(String paramString, boolean paramBoolean) {
		System.out.println(usbflash.toString());
		return usbflash._Java_com_sonyericsson_cs_usbflashnative_impl_USBFlashNativeImpl_openChannel(paramString, paramBoolean);
	}

}