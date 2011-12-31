package org.system;

import javax.swing.event.EventListenerList;

import org.system.Device;
import org.system.DeviceIdent;
import org.system.StatusEvent;
import org.system.StatusListener;

public class PhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	boolean forced = false;
	String status = "";
	
	private final EventListenerList listeners = new EventListenerList();

	public void run() {
		int count = 0;
		int nbnull = 0;
		DeviceIdent id=null;
		while (!done) {
			if (!paused) {
				id = Device.getConnectedDevice();
				if (id.getPid().equals("ADDE"))
					GlobalState.setState(id.getSerial(), id.getPid(), "flash");
				else if (id.getPid().equals("0DDE"))
					GlobalState.setState(id.getSerial(), id.getPid(), "fastboot");
				while (id.getStatus().length()==0) {
					doSleep(300);
					id = Device.getConnectedDevice();
					nbnull++;
					if (nbnull==5) {
						nbnull=0;
	    				GlobalState.setState(id.getSerial(), id.getPid(), "normal");
						break;
					}
				}
				String lstatus= id.getStatus();
				if (!lstatus.equals(status)) {
					if (!lstatus.equals("adb"))
						fireStatusChanged(new StatusEvent(lstatus,id.isDriverOk()));
					status = lstatus;
				}
			}
			try {
				while ((count<50) && (!done)) {
					sleep(10);
					count++;
				}
				count = 0;					
			} catch (Exception e) {}
		}
		Device.clean();
	}

	public void pause(boolean ppaused) {
		paused = ppaused;
		status="paused";
	}

	public void end() {
		done = true;
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
    
    public void forceDetection() {
    	if (!forced) forced = true;
    }
    
    public void doSleep(int time) {
    	try {
    		sleep(time);
    	}
    	catch (Exception e) {}
    }
}
