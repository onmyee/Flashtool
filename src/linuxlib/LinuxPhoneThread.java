package linuxlib;

import javax.swing.event.EventListenerList;

import org.system.Device;
import org.system.DeviceIdent;
import org.system.Devices;
import org.system.StatusEvent;
import org.system.StatusListener;

public class LinuxPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	boolean forced = false;
	String status = "";
	private final EventListenerList listeners = new EventListenerList();
	
	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceLinux();
				if (forced) {
					fireStatusChanged(new StatusEvent(id.getStatus(),id.isDriverOk()));
				}
				else
				if (!status.equals(id.getStatus())) {
					fireStatusChanged(new StatusEvent(id.getStatus(),id.isDriverOk()));
					status = id.getStatus();
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
	}

	public void pause(boolean ppaused) {
		paused = ppaused;
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
}