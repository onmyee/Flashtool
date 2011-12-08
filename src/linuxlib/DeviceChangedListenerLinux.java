package linuxlib;

import linuxlib.LinuxPhoneThread;

public class DeviceChangedListenerLinux  {

	public static LinuxPhoneThread usbwatch;
	
	public DeviceChangedListenerLinux() {
		usbwatch = new LinuxPhoneThread();
		usbwatch.start();
	}
	
}
