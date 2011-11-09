package org.adb;

import org.system.OS;
import org.system.OsRun;

public class FastbootUtility {

	private static String adbpath = OS.getAdbPath();
	private static String fastbootpath = OS.getFastBootPath();
	
	public static void adbRebootFastboot() throws Exception {
		OsRun command = new OsRun(adbpath+" reboot bootloader");
		command.run();
	}
	
	public static void hotBoot(String bootimg) throws Exception {
		OsRun command = new OsRun(fastbootpath+" boot "+bootimg);
		command.run();		
	}
	
	public static void flashBoot(String bootimg) throws Exception {
		OsRun command = new OsRun(fastbootpath+" flash boot "+bootimg);
		command.run();		
	}
	
	public static void rebootDevice() throws Exception {
		OsRun command = new OsRun(fastbootpath+" reboot");
		command.run();
	}
	
	public static void rebootFastboot() throws Exception {
		OsRun command = new OsRun(fastbootpath+" reboot-bootloader");
		command.run();
	}
	
	public static void wipeDataCache() throws Exception {
		OsRun command = new OsRun(fastbootpath+" -w");
		command.run();
	}
	
	public static void getDeviceInfo() throws Exception {
		OsRun command = new OsRun(fastbootpath+" devices");
		command.run();
	}
	
	public static void getFastbootVerInfo() throws Exception {
		OsRun command = new OsRun(fastbootpath+" -i 0x0fce getvar version");
		command.run();
	}
}
