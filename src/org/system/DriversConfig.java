package org.system;

import java.util.Collection;
import java.util.HashMap;
import java.awt.List;

public class DriversConfig {
	private static PropertiesFile config;
	private static List adb = new List();
	private static HashMap fastboot;
	private static HashMap flash;
	private static String fsep = OS.getFileSeparator();
	
	private static void init() {
		if (config==null) {
			config = new PropertiesFile("",OS.getWorkDir()+fsep+"x10flasher_lib"+fsep+"driverid.properties");
			adb.
		}
	}

}
