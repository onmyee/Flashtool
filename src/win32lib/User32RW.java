package win32lib;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.win32.W32APIOptions;


public interface User32RW extends User32 {

    public static final User32RW MYINSTANCE = (User32RW) Native.loadLibrary("user32", User32RW.class, W32APIOptions.UNICODE_OPTIONS);

    /**
     * Sets a new address for the window procedure (value to be set).
     */
    public static final int GWLP_WNDPROC = -4;

    /**
     * Changes an attribute of the specified window
     * @param   hWnd        A handle to the window
     * @param   nIndex      The zero-based offset to the value to be set.
     * @param   callback    The callback function for the value to be set.
     */
    public int SetWindowLong(WinDef.HWND hWnd, int nIndex, Callback callback);
    
    public int DefWindowProc(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM uParam, WinDef.LPARAM lParam);

}