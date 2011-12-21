package linuxlib;

import javax.swing.event.EventListenerList;

import org.system.Device;
import org.system.DeviceIdent;
import org.system.Devices;
import org.system.StatusListener;

public class LinuxPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	String status = "";
	private final EventListenerList listeners = new EventListenerList();
	
	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceLinux();
				if (!status.equals(id.getStatus())) {
					status = id.getStatus();
					if (!Devices.isWaitingForReboot())
						Device.identDevice(id.getStatus(),id.isDriverOk());
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
    
}