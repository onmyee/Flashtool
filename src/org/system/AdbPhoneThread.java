package org.system;

import java.io.InputStream;
import java.util.Scanner;

import javax.swing.event.EventListenerList;

import org.adb.AdbUtility;

public class AdbPhoneThread extends Thread {
	
	String currentPid="none";
	boolean done=false;

	private ProcessBuilder builder;
	private Process adb;
	private InputStream processInput;
	private Scanner sc;
	private final EventListenerList listeners = new EventListenerList();
	private StatusListener listener;
	boolean first = true;

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
				      DeviceIdent id = null;
				      DeviceIdent newid = null;
			    	  while (sc.hasNextLine()) {
			    		  String line = sc.nextLine();
			    		  if (line.contains("State")) {
				    		  if (line.contains("device")) {
				    			  id = Device.getLastConnected(first);
				    			  newid = Device.getLastConnected(first);
				    			  if (!GlobalState.getState(newid.getSerial(), newid.getPid()).equals("adb")) {
				    				  int count=0;
				    				  if (first) count=19;
				    				  while (newid.getPid().equals(id.getPid())) {
				    					  Sleep(100);
				    					  count++;
				    					  newid = Device.getLastConnected(first);
				    					  if (count==20) break;
				    				  }
				    				  GlobalState.setState(newid.getSerial(), newid.getPid(), "adb");
				    			  }
				    			  fireStatusChanged(new StatusEvent(GlobalState.getState(newid.getSerial(), newid.getPid()),true));
				    		  }
				    		  if (first) {
				    			  DeviceChangedListener.start();
				    			  DeviceChangedListener.addStatusListener(listener);
				    			  first = false;
				    		  }
			    		  }
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

	private void Sleep(int ms) {
		try {
			sleep(ms);
		}
		catch (Exception e) {
		}
	}
	public void addStatusListener(StatusListener plistener) {
        listeners.add(StatusListener.class, plistener);
        listener = plistener;
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

    public void doSleep(int time) {
    	try {
    		sleep(time);
    	}
    	catch (Exception e) {}
    }

}