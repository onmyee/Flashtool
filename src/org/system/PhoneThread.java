package org.system;

import java.io.InputStream;
import java.util.Scanner;

import javax.swing.event.EventListenerList;

import org.adb.AdbUtility;

public class PhoneThread extends Thread {
	
	String currentPid="none";
	boolean done=false;

	private ProcessBuilder builder;
	private Process adb;
	private InputStream processInput;
	private Scanner sc;
	private final EventListenerList listeners = new EventListenerList();

	public void done() {
		done=true;
	}
	
	public void run() {
		try {
			builder = new ProcessBuilder(OS.getAdbPath(), "status-window");
			adb = builder.start();
		    Thread t = new Thread() {
		    	  public void run() {
				      processInput = adb.getInputStream();
				      sc = new Scanner(processInput);
			    	  while (sc.hasNextLine()) {
			    		  String line = sc.nextLine();
			    		  if (line.contains("device"))
			    			  fireStatusChanged(new StatusEvent("adb",true));
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

	public void addStatusListener(StatusListener listener) {
        listeners.add(StatusListener.class, listener);
    }

    public void removeStatusListener(StatusListener listener) {
        listeners.remove(StatusListener.class, listener);
    }

    public StatusListener[] getStatusListeners() {
        return listeners.getListeners(StatusListener.class);
    }

    protected void fireStatusChanged(StatusEvent e) {
		for(StatusListener listener : getStatusListeners()) {
		    listener.statusChanged(e);
		}
    }

}