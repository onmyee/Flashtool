package win32lib;

import org.system.Device;
import org.system.DeviceIdent;

public class WindowsPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	String pid = "";

	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceWin32();
				if (!pid.equals(id.getPid())) {
					pid = id.getPid();
					Device.identDevice();
				}
			}
			try {
				while ((count<200) && (!done)) {
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

}
