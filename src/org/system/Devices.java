package org.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class Devices  {

	private static DeviceEntry _current=null;;
	private static Properties props = null;
	private static boolean waitforreboot=false;
	
	public static Enumeration listDevices(boolean reload) {
		if (reload || props==null) load();
		return props.keys();
	}
	
	public static DeviceEntry getDevice(String device) {
		return (DeviceEntry)props.get(device);
	}
	
	public static void setCurrent(String device) {
		AdbUtility.init();
		_current = (DeviceEntry)props.get(device);
		_current.queryAll();
	}
	
	public static DeviceEntry getCurrent() {
		return _current;
	}
	
	private static void load() {
		if (props==null) props=new Properties();
		else props.clear();
		File[] list = (new File("./devices")).listFiles();
		if (list==null) return;
		for (int i=0;i<list.length;i++) {
			if (list[i].isDirectory()) {
				Properties p = new Properties();
				String device = list[i].getPath().substring(list[i].getPath().lastIndexOf(OS.getFileSeparator())+1);
				try {
					p.load(new FileInputStream(new File(list[i].getPath()+OS.getFileSeparator()+device+".properties")));
					DeviceEntry entry = new DeviceEntry(p);
					if (device.equals(entry.getId()))
						props.put(device, entry);
					else MyLogger.error(device + " : this bundle is not valid");
				}
				catch (FileNotFoundException fne) {
					MyLogger.error(device + " : this bundle is not valid");
				}
				catch (IOException ioe) {
				}
			}
		}
	}

	public static void waitForReboot() throws Exception {
		MyLogger.info("Waiting for device");
		waitforreboot=true;
		while (waitforreboot) Thread.sleep(1000);
	}
	
	public static void stopWaitForReboot() {
		waitforreboot=false;
	}
}