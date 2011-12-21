package win32lib;

import javax.swing.event.EventListenerList;

import org.system.Device;
import org.system.DeviceIdent;
import org.system.StatusEvent;
import org.system.StatusListener;

public class WindowsPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	boolean forced = false;
	String pid = "";
	String status = "";
	
	private final EventListenerList listeners = new EventListenerList();

	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceWin32();
				String lpid = id.getPid();
				if (!pid.equals(lpid)) {
					if (lpid.equals("ADDE"))
						fireStatusChanged(new StatusEvent("flash",id.isDriverOk()));
					else if (lpid.equals("0DDE"))
						fireStatusChanged(new StatusEvent("fastboot",id.isDriverOk()));
					else if (lpid.equals(""))
						fireStatusChanged(new StatusEvent("none",id.isDriverOk()));
				}
				pid=lpid;
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
