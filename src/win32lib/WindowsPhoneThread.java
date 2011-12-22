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
	String lastfire="";
	
	private final EventListenerList listeners = new EventListenerList();

	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceWin32();
				String lpid = id.getPid();
				String lfire="";
				if (!pid.equals(lpid)) {
					if (lpid.equals("ADDE"))
						lfire="flash";
					else if (lpid.equals("0DDE"))
						lfire="fastboot";
					else if (lpid.equals(""))
						lfire="none";
					if (lfire.length()>0) {
						if (!lfire.equals(lastfire)) {
							fireStatusChanged(new StatusEvent(lfire,id.isDriverOk()));
							lastfire=lfire;
						}
					}
					pid=lpid;
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
