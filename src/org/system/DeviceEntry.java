package org.system;

import gui.BusyBoxSelectGUI;
import gui.RecoveryBootSelectGUI;

import java.util.HashSet;
import java.util.Properties;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class DeviceEntry {


	Properties _entry;
	private static String fsep = OS.getFileSeparator();
	private Boolean hasBusybox=null;
	private boolean isRecoveryMode=false;
	
	public void queryAll() {
		setVersion();
		setKernelVersion();
		try {
			isRecoveryMode=!AdbUtility.isMounted("/system");
		}
		catch (Exception e) {
		}
	}

	public boolean hasRoot() {
		if (AdbUtility.hasRootNative()) return AdbUtility.hasRootNative();
		return AdbUtility.hasRootPerms();
	}

	public boolean isRecovery() {
		return isRecoveryMode;
	}

	public boolean hasSU() {
		try {
		return AdbUtility.hasSU();
		}
		catch (Exception e) {
			return false;
		}
	}

	public void rebootSelectedRecovery() throws Exception {
		RecoveryBootSelectGUI rsel = new RecoveryBootSelectGUI();
		rsel.setTitle("Recovery selector");
		String current = rsel.getVersion();
		if (current.length()>0) {
			MyLogger.info("Rebooting into recovery mode");
			Shell shell = new Shell("rebootrecoveryt");
			shell.setProperty("RECOV_VERSION", current);
			shell.runRoot();
			MyLogger.info("Phone will reboot into recovery mode");
		}
		else {
			MyLogger.info("Canceled");
		}
	}
	
	public void setDefaultRecovery() throws Exception {
		RecoveryBootSelectGUI rsel = new RecoveryBootSelectGUI();
		String current = rsel.getVersion();
		if (current.length()>0) {
			if (AdbUtility.Sysremountrw()) {
			MyLogger.info("Setting default recovery");
			Shell shell = new Shell("setdefaultrecovery");
			shell.setProperty("RECOV_VERSION", current);
			shell.runRoot();
			MyLogger.info("Done");
			}
		}
		else {
			MyLogger.info("Canceled");
		}
	}
	
	private void setKernelVersion() {
		_entry.setProperty("kernel.version", AdbUtility.getKernelVersion(isBusyboxInstalled()));
	}
	
	public String getKernelVersion() {
		return _entry.getProperty("kernel.version");
	}
	
	public DeviceEntry(Properties entry) {
		_entry = entry;
	}
	
	public String getId() {
		return _entry.getProperty("internalname");
	}
	
	public String getName() {
		return _entry.getProperty("realname");
	}
	
	public String getWorkDir() {
		return "."+fsep+"devices"+fsep+getId()+fsep+"work";
	}
	
	public String getBuildProp() {
		return _entry.getProperty("buildprop");
	}
	
	public String getLoaderMD5() {
		return _entry.getProperty("loader").toUpperCase();
	}

	public String getLoaderUnlockedMD5() {
		return _entry.getProperty("loader_unlocked").toUpperCase();
	}

	public boolean hasUnlockedLoader() {
		return _entry.containsKey("loader_unlocked");
	}

	public String getBusyBoxInstallPath() {
		return _entry.getProperty("busyboxinstallpath");
	}
	
	public String getInstalledBusyboxVersion() {
		if (Devices.getCurrent().isBusyboxInstalled()) {
			return AdbUtility.getBusyboxVersion(getBusyBoxInstallPath());
		}
		else 
			return "N/A";

	}
	
	public HashSet<String> getRecognitionList() {
		String[] result = _entry.getProperty("recognition").split(",");
		HashSet<String> set = new HashSet<String>();
		for (int i=0;i<result.length;i++) {
			set.add(result[i]);
		}
		return set;
	}
	
	public String getLoader() {
		return "./devices/"+_entry.getProperty("internalname")+"/loader.sin";
	}

	public String getLoaderUnlocked() {
		return "./devices/"+_entry.getProperty("internalname")+"/loader_unlocked.sin";
	}
	
	private void setVersion () {
		_entry.setProperty("android.release",AdbUtility.getProperty("ro.build.version.release"));
	}
	
	public String getVersion() {
		return _entry.getProperty("android.release");
	}
	
	public boolean canFlash() {
		return _entry.getProperty("canflash").equals("true");
	}
	
	public boolean canKernel() {
		return _entry.getProperty("cankernel").equals("true");
	}

	public boolean canRecovery() {
		return _entry.getProperty("canrecovery").equals("true");
	}

	public String getBusybox(boolean select) {
		String version;
		if (!select) version = _entry.getProperty("busyboxhelper");
		else {
			BusyBoxSelectGUI sel = new BusyBoxSelectGUI(getId());
			version = sel.getVersion();
		}
		if (version.length()==0) return "";
		else return "."+fsep+"devices"+fsep+_entry.getProperty("internalname")+fsep+"busybox"+fsep+version+fsep+"busybox";
	}
	
	public String getOptimize() {
		return "./devices/"+_entry.getProperty("internalname")+"/optimize.tar";
	}
	
	public String getBuildMerge() {
		return "./devices/"+_entry.getProperty("internalname")+"/build.prop";
	}

	public String getCharger() {
		return "./devices/"+_entry.getProperty("internalname")+"/charger";
	}

	public boolean isBusyboxInstalled() {
    	if (hasBusybox==null)
    		hasBusybox = (AdbUtility.getBusyboxVersion(getBusyBoxInstallPath()).length()>0);
    	return hasBusybox.booleanValue();
    }

    public void doBusyboxHelper() throws Exception {
    	if (!isBusyboxInstalled()) {
    		AdbUtility.push(getBusybox(false), GlobalConfig.getProperty("deviceworkdir")+"/busybox");
    		Shell shell = new Shell("busyhelper");
    		shell.run(true);
		}
    }

}
