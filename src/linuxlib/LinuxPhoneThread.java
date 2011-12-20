package linuxlib;

import org.system.Device;
import org.system.DeviceIdent;
import org.system.Devices;

public class LinuxPhoneThread extends Thread {

	boolean done = false;
	boolean paused = false;
	String pid = "";

	public void run() {
		int count = 0;
		while (!done) {
			if (!paused) {
				DeviceIdent id = Device.getConnectedDeviceLinux();
				if (!pid.equals(id.getPid())) {
					pid = id.getPid();
					if (!Devices.isWaitingForReboot())
						Device.identDevice();
				}
			}
			try {
				while ((count<100) && (!done)) {
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