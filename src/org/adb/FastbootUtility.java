package org.adb;

import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;

import org.system.OS;
import org.system.OsRun;
import org.system.RunOutputs;

public class FastbootUtility {

	private static String adbpath = OS.getAdbPath();
	private static String fastbootpath = OS.getFastBootPath();
	
	public static void adbRebootFastboot() throws Exception {
		OsRun command = new OsRun(adbpath+" reboot bootloader");
		command.run();
	}
	
	public static Enumeration<String> getDevices() {
		Vector<String>  v = new Vector<String>();
		try {
			OsRun command = new OsRun(fastbootpath+" devices");
			command.run();
			Scanner sc = new Scanner(command.getStdOut());
			while (sc.hasNextLine()) {
				v.add(sc.nextLine());
			}
		}
		catch (Exception e) {
		}
		return v.elements();
	}

	public static RunOutputs hotBoot(String bootimg) throws Exception {
		OsRun command = new OsRun(fastbootpath+" boot \""+bootimg+"\"");
		command.run();	
		return command.getOutputs();
	}
	
	public static RunOutputs flashBoot(String bootimg) throws Exception {
		OsRun command = new OsRun(fastbootpath+" flash boot \""+bootimg+"\"");
		command.run();		
		return command.getOutputs();
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
		// currently there seems to be some issue executing this
		OsRun command = new OsRun(fastbootpath+" -w");
		command.run();
	}
	
	public static RunOutputs getDeviceInfo() throws Exception {
		OsRun command = new OsRun(fastbootpath+" devices");
		command.run();
		return command.getOutputs();
	}
	
	public static RunOutputs getFastbootVerInfo() throws Exception {
		OsRun command = new OsRun(fastbootpath+" -i 0x0fce getvar version");
		command.run();
		return command.getOutputs();
	}

	public static void killFastbootWindows() {
		try {
			OsRun fastboot = new OsRun("taskkill /F /T /IM fastboot*");
			fastboot.run();
		}
		catch (Exception e) {
		}		
	}

}
