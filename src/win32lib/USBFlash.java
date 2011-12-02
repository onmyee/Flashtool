package win32lib;

import com.sun.jna.Library;
import com.sun.jna.win32.StdCallLibrary;

public interface USBFlash extends StdCallLibrary {

	int _Java_com_sonyericsson_cs_usbflashnative_impl_USBFlashNativeImpl_openChannel(String paramString, boolean paramBoolean);

}