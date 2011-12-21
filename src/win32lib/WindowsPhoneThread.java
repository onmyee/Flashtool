package win32lib;

import javax.swing.event.EventListenerList;

import org.logger.MyLogger;
import org.system.Device;
import org.system.DeviceIdent;
import org.system.Devices;
import org.system.StatusEvent;
import org.system.StatusListener;

public class WindowsPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	String status = "";
	private final EventListenerList listeners = new EventListenerList();

	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceWin32();
				if (!status.equals(id.getStatus())) {
					status = id.getStatus();
					fireStatusChanged(new StatusEvent(status,id.getStatus()));
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
    	if (e.hasChanged())
            for(StatusListener listener : getStatusListeners()) {
                listener.statusChanged(e);
            }
    }
}
