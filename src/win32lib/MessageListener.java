package win32lib;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface MessageListener extends StdCallCallback {

    public int callback(HWND hWnd, int uMsg, WPARAM uParam, LPARAM lParam);

}
