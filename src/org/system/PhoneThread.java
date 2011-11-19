package org.system;

import gui.FlasherGUI;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class PhoneThread extends Thread {
	
	String currentStatus="none";
	boolean done=false;
	
	public void done() {
		done=true;
	}
	
	public void run() {
		String status = "none";
		if (OS.getName().equals("windows")) {
		try {
			while (!done) {
				try {
					sleep(2000);
				}
		    	catch (InterruptedException ie) {
		    	}
				if (!done) {
				currentStatus=Device.getStatus(Device.getConnectedDevice());
				if (!status.equals(currentStatus)) {
					status = currentStatus;
					if (currentStatus.startsWith("Err")) {
						MyLogger.getLogger().error("Device connected in "+currentStatus.replace("Err","")+" mode.");
						MyLogger.getLogger().error("Drivers need to be installed for connected device.");
					}
					if (currentStatus.equals("adb")) {
		    		  if (!AdbUtility.isConnected()) {
			    		  while (!AdbUtility.isConnected()) {
			    			  sleep(1000);
			    		  }
		    		  }
		    		  MyLogger.getLogger().info("Device connected with USB debugging on");
		    		  MyLogger.getLogger().debug("Device connected, continuing with identification");
		    		  FlasherGUI.doIdent();
					}
					if (currentStatus.equals("none")) {
						MyLogger.getLogger().info("Device disconnected");
						FlasherGUI.doDisableIdent();
					}
					if (currentStatus.equals("flash")) {
						MyLogger.getLogger().info("Device connected in flash mode");
						FlasherGUI.doDisableIdent();
					}
					if (currentStatus.equals("fastboot")) {
						MyLogger.getLogger().info("Device connected in fastboot mode");
						FlasherGUI.doDisableIdent();
					}
					if (currentStatus.equals("normal")) {
						MyLogger.getLogger().info("Device connected with USB debugging off");
						FlasherGUI.doDisableIdent();
					}
				}
				}
			}
		}
		catch (Exception e) {
		}
		}
	}

}