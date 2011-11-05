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

}
