package org.system;

import gui.FlasherGUI;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class AdbThread extends Thread {

	private ProcessBuilder builder;
	private Process adb;
	private static String fsep = OS.getFileSeparator();
	private InputStream processInput;
	private Scanner sc;
	boolean done = false;
	
	public void run() {
		try {
			if (OS.getName().equals("linux"))
				builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb").getAbsolutePath(), "status-window");
			else
				builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb.exe").getAbsolutePath(), "status-window");
			adb = builder.start();
		    Thread t = new Thread() {
		    	  public void run() {
				      processInput = adb.getInputStream();
				      sc = new Scanner(processInput);
			    	  while (sc.hasNextLine()) {
			    		  String line = sc.nextLine();
			    		  if (OS.getName().equals("linux")) {
			    			  if (line.contains("State: device")) {
			    				  if (!AdbUtility.isConnected()) { 
			    					  while (!AdbUtility.isConnected()) {
			    						  try {
			    							  sleep(1000);
			    						  }
			    						  catch (Exception e) {
			    						  }
			    					  }
			    				  }
			    				  MyLogger.getLogger().debug("Device connected, continuing with identification");
			    				  FlasherGUI.doIdent();
			    			  }
			    			  else
			    				  if (line.contains("State: unknown")) FlasherGUI.doDisableIdent();
			    		  }
			    	  }    		  
		    	  }
		    };
		    t.start();
		    while (!done) {
		    	try {
		    		sleep(2000);
		    	}
		    	catch (InterruptedException ie) {
		    	}
		    }
			adb.destroy();
			try {
				AdbUtility.killServer();
			}
			catch (Exception e) {
			}
		} catch (Exception e) {
		}
	}
	
	public void done() {
		done=true;
	}
}