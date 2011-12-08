package win32lib;


public class DeviceChangedListenerWin32  {

	public static WindowsPhoneThread usbwatch;
	
	public DeviceChangedListenerWin32() {
		usbwatch = new WindowsPhoneThread();
		usbwatch.start();
	}
	
}