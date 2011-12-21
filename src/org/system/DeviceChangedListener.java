package org.system;

import linuxlib.DeviceChangedListenerLinux;
import win32lib.DeviceChangedListenerWin32;

public class DeviceChangedListener {
	
	static DeviceChangedListenerWin32 lwin32=null;
	static DeviceChangedListenerLinux llinux=null;
	
	public DeviceChangedListener() {
		if (OS.getName().equals("windows")) {
			lwin32 = new DeviceChangedListenerWin32();
		}
		else {
			llinux = new DeviceChangedListenerLinux();
		}
	}
	
	public static void start() {
		if (OS.getName().equals("windows")) {
			lwin32 = new DeviceChangedListenerWin32();
		}
		else {
			llinux = new DeviceChangedListenerLinux();
		}		
	}
	
	public static void stop() {
		try {
			if (OS.getName().equals("windows")) {
				if (lwin32!=null)
					DeviceChangedListenerWin32.usbwatch.end();
					DeviceChangedListenerWin32.usbwatch.join();
			}
			else {
				if (llinux!=null)
					DeviceChangedListenerLinux.usbwatch.end();
					DeviceChangedListenerLinux.usbwatch.join();
			}
		}
		catch (Exception e) {}
	}
	
	public static void pause(boolean paused) {
		if (OS.getName().equals("windows")) {
			if (lwin32!=null)
				lwin32.usbwatch.pause(paused);
		}
		else {
			if (llinux!=null)
				llinux.usbwatch.pause(paused);
		}
	}

	public static void addStatusListener(StatusListener listener) {
		if (OS.getName().equals("windows")) {
			if (lwin32!=null)
				lwin32.usbwatch.addStatusListener(listener);
		}
		else {
			if (llinux!=null)
				llinux.usbwatch.addStatusListener(listener);
		}		
	}
}