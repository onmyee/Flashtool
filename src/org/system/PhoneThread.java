package org.system;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import gui.FlasherGUI;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class PhoneThread extends Thread {
	
	String currentPid="none";
	boolean done=false;

	private ProcessBuilder builder;
	private Process adb;
	private static String fsep = OS.getFileSeparator();
	private InputStream processInput;
	private Scanner sc;

	public void done() {
		done=true;
	}
	
	public void run() {
		String pid = "none";
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
			    	  }    		  
		    	  }
		    };
		    t.start();
			while (!done) {
				int count=1;
				while (count<2001 && !done) {
					sleep(1);
					count++;
				}
			}
			adb.destroy();
			try {
				AdbUtility.killServer();
			}
			catch (Exception e) {
			}
		}
		catch (Exception e) {
		}
	}

}