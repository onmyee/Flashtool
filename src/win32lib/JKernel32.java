package win32lib;

import java.io.IOException;

import org.logger.MyLogger;
import org.system.Device;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.OVERLAPPED;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

public class JKernel32 {

	public static Kernel32RW kernel32 = (Kernel32RW) Native.loadLibrary("kernel32", Kernel32RW.class, W32APIOptions.UNICODE_OPTIONS);
	static WinNT.HANDLE HandleToDevice = WinBase.INVALID_HANDLE_VALUE;
	static byte[] buffer = new byte[1];
	public static final OVERLAPPED overlapped = new OVERLAPPED();

	public static boolean openDevice() {
        /* Kernel32RW.GENERIC_READ | Kernel32RW.GENERIC_WRITE not used in dwDesiredAccess field for system devices such a keyboard or mouse */
        int shareMode = WinNT.FILE_SHARE_READ | WinNT.FILE_SHARE_WRITE;
        int Access = WinNT.GENERIC_WRITE | WinNT.GENERIC_READ;
		HandleToDevice = Kernel32.INSTANCE.CreateFile(
                Device.getConnectedDevice().getDevPath(), 
                Access, 
                shareMode, 
                null, 
                WinNT.OPEN_EXISTING, 
                WinNT.FILE_FLAG_OVERLAPPED, 
                (WinNT.HANDLE)null);
		return (HandleToDevice != WinBase.INVALID_HANDLE_VALUE);
	}
	
	public static byte[] readBytes() throws IOException {
		int bufsize=0x10000;
		IntByReference nbread = new IntByReference();
		OVERLAPPED ov = new OVERLAPPED();
		ov.Offset=0;
		ov.OffsetHigh=0;
		byte[] b = new byte[bufsize];
		boolean result = kernel32.ReadFile(HandleToDevice, b, bufsize, nbread, ov);
		if (!result) {
			int lasterror =getLastErrorCode(); 
			if (lasterror==Kernel32.ERROR_IO_PENDING) {
				while (!kernel32.GetOverlappedResult(HandleToDevice, ov, nbread, true)) {
					System.out.print(getLastError());
				}
			}
			else {
				String err = getLastError();
				closeDevice();
				throw new IOException("readBytes \\ "+err);
			}
		}
		return getReply(b,nbread.getValue());
	}

	private static byte[] getReply(byte[] reply, int nbread) {
		byte[] newreply=null;
		if (nbread > 0) {
			newreply = new byte[nbread];
			System.arraycopy(reply, 0, newreply, 0, nbread);
		}
		return newreply;
	}
	
	public static boolean writeBytes(byte bytes[]) throws IOException {
		IntByReference nbwritten = new IntByReference();
		OVERLAPPED ov = new OVERLAPPED();
		ov.Offset=0;
		ov.OffsetHigh=0;
		boolean result = kernel32.WriteFile(HandleToDevice, bytes, bytes.length, nbwritten, ov);
		if (!result) {
			int lasterror =getLastErrorCode(); 
			if (lasterror==Kernel32.ERROR_IO_PENDING) {
				while (!kernel32.GetOverlappedResult(HandleToDevice, ov, nbwritten, true)) {
				}
				result = nbwritten.getValue()==bytes.length;
			}
			else {
				String err = getLastError();
				closeDevice();
				throw new IOException("writeBytes \\ "+err);
			}
		}
		return result;
	}

	public static boolean closeDevice() {
		boolean result = true;
		MyLogger.getLogger().info("Closing USB device");
		if (HandleToDevice != WinBase.INVALID_HANDLE_VALUE)
			result = kernel32.CloseHandle(HandleToDevice);
		HandleToDevice = WinBase.INVALID_HANDLE_VALUE;
		return result;
	}
	
	public static int getLastErrorCode() {
		return Kernel32.INSTANCE.GetLastError();
	}
	
	public static String getLastError() {
		int code = Kernel32.INSTANCE.GetLastError();
	    Kernel32 lib = Kernel32.INSTANCE;
	    PointerByReference pref = new PointerByReference();
	    lib.FormatMessage(
	        WinBase.FORMAT_MESSAGE_ALLOCATE_BUFFER | WinBase.FORMAT_MESSAGE_FROM_SYSTEM | WinBase.FORMAT_MESSAGE_IGNORE_INSERTS, 
	        null, 
	        code, 
	        0, 
	        pref, 
	        0, 
	        null);
	    String s = code + " : " +pref.getValue().getString(0, !Boolean.getBoolean("w32.ascii"));
	    lib.LocalFree(pref.getValue());
	    return s.replaceAll("[\n\r]+"," ");
	}

}
