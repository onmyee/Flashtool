package org.adb;

import org.system.OS;
import org.system.OsRun;

public class FastbootUtility {
	
	private static String fsep = OS.getFileSeparator();
	
	public static void adbRebootFastboot() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"adb reboot bootloader");
		command.run();
	}
	
	public static void hotBoot(String bootimg) throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot boot "+bootimg);
		command.run();		
	}
	
	public static void flashBoot(String bootimg) throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot flash boot "+bootimg);
		command.run();		
	}
	
	public static void rebootDevice() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot reboot");
		command.run();
	}
	
	public static void rebootFastboot() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot reboot-bootloader");
		command.run();
	}
	
	public static void wipeDataCache() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot -w");
		command.run();
	}
	
	public static void getDeviceInfo() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot devices");
		command.run();
	}
	
	public static void getFastbootVerInfo() throws Exception {
		OsRun command = new OsRun("."+fsep+"x10flasher_lib"+fsep+"fastboot.exe -i 0x0fce getvar version");
		command.run();
	}
}
