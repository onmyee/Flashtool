package win32lib;

import com.sun.jna.Library;

public interface USBFlash extends Library {

	int Java_com_sonyericsson_cs_subflashnative_impl_USBFlashNativeImpl_openChannel(String paramString, boolean paramBoolean);

}