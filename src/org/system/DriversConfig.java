package org.system;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class DriversConfig {
	
	private static PropertiesFile config=null;
	private static Vector<String> adb = new Vector<String>();
	private static Vector<String> fastboot = new Vector<String>();
	private static Vector<String> flash = new Vector<String>();
	private static Vector<String> normal = new Vector<String>();
	private static String fsep = OS.getFileSeparator();

	private static void init() {
		if (config==null) {
			config = new PropertiesFile("",OS.getWorkDir()+fsep+"x10flasher_lib"+fsep+"driverid.properties");
			adb.clear();
			fastboot.clear();
			flash.clear();
			normal.clear();
			Iterator i = config.keySet().iterator();
			while (i.hasNext()) {
				String key = (String)i.next();
				if (key.startsWith("adb")) adb.add(config.getProperty(key));
				if (key.startsWith("fastboot")) fastboot.add(config.getProperty(key));
				if (key.startsWith("flash")) flash.add(config.getProperty(key));
				if (key.startsWith("normal")) normal.add(config.getProperty(key));
			}
		}
	}

	public static Enumeration<String> getAdb() {
		init();
		return adb.elements();
	}

	public static Enumeration<String> getFastboot() {
		init();
		return fastboot.elements();
	}

	public static Enumeration<String> getFlash() {
		init();
		return flash.elements();
	}

	public static Enumeration<String> getNormal() {
		init();
		return normal.elements();
	}

}
