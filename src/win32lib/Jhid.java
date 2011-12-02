package win32lib;

import win32lib.Hid.HIDD_ATTRIBUTES;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public class Jhid {

	static Hid hid = (Hid) Native.loadLibrary("hid", Hid.class, W32APIOptions.UNICODE_OPTIONS);

	public static int getAttributes(WinNT.HANDLE Handle, Hid.HIDD_ATTRIBUTES HIDAttributes) {
		return hid.HidD_GetAttributes(Handle, HIDAttributes);
	}
	
}
