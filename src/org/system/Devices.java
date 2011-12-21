package org.system;

import gui.FlasherGUI;

import java.io.File;
import java.util.Properties;
import java.util.Enumeration;

import org.adb.AdbUtility;
import org.adb.FastbootUtility;
import org.logger.MyLogger;

public class Devices  {

	private static DeviceEntry _current=null;;
	private static Properties props = null;
	private static boolean waitforreboot=false;

	public static boolean HasOneAdbConnected() {
		return AdbUtility.isConnected();
	}
	
	public static boolean HasOneFastbootConnected() {
		return FastbootUtility.getDevices().hasMoreElements();
	}

	public static Enumeration<Object> listDevices(boolean reload) {
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
		File[] list = (new File(OS.getWorkDir()+OS.getFileSeparator()+"devices")).listFiles();
		if (list==null) return;
		for (int i=0;i<list.length;i++) {
			if (list[i].isDirectory()) {
				PropertiesFile p = new PropertiesFile();
				String device = list[i].getPath().substring(list[i].getPath().lastIndexOf(OS.getFileSeparator())+1);
				try {
					p.open("",new File(list[i].getPath()+OS.getFileSeparator()+device+".properties").getAbsolutePath());
					DeviceEntry entry = new DeviceEntry(p);
					if (device.equals(entry.getId()))
						props.put(device, entry);
					else MyLogger.getLogger().error(device + " : this bundle is not valid");
				}
				catch (Exception fne) {
					MyLogger.getLogger().error(device + " : this bundle is not valid");
				}
			}
		}
	}

	public static void waitForReboot() throws Exception {
		MyLogger.getLogger().info("Waiting for device");
		waitforreboot=true;
		while (waitforreboot) {
			Thread.sleep(20);
		}
	}

	public static void stopWaitForReboot() {
		waitforreboot=false;
	}
	
	public static boolean isWaitingForReboot() {
		return waitforreboot;
	}
}