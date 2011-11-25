package org.system;

import java.util.HashSet;

import org.adb.AdbUtility;
import org.adb.FastbootUtility;

public class DeviceIdent {

	private String pid;
	private String vid;
	private String devid;
	private boolean isDriverOk;
	
	public DeviceIdent() {
		pid="";
		vid="";
		devid="";
		isDriverOk=false;
	}
	public DeviceIdent(String device) {
		String tmppid=device.substring(device.indexOf("PID"),device.indexOf("PID")+12);
		pid=tmppid.substring(4,tmppid.indexOf("\\"));
		String tmpvid=device.substring(device.indexOf("VID"),device.indexOf("VID")+12);
		vid=tmpvid.substring(4,tmppid.indexOf("\\"));
	}

	public void setDriverOk(boolean status) {
		isDriverOk = status;
	}
	
	public String getPid() {
		return pid;
	}

	public String getVid() {
		return vid;
	}
	
	public boolean isDriverOk() {
		return isDriverOk;
	}
	
	public String getDeviceId() {
		return devid;
	}

	public String getStatus() {
		
		String err="";
    	String status = "none";
	    	if (!devid.equals("none")) {
	    		DeviceIdent i = new DeviceIdent(device);
	    		System.out.println(i.getDeviceId());
	    		if (!i.isDriverOk())
	    			err = "Err";
	    		if (i.getPid().equals("ADDE")) status="flash";
	    		else if (AdbUtility.getDevices().hasMoreElements())
	    			status="adb";
	    		else if (FastbootUtility.getDevices().hasMoreElements())
	    			status="fastboot";
	    		else {
	    			if (i.getPid().startsWith("0") || i.getPid().startsWith("E")) 
	    				status="normal";
	    			else
	    				status="mtp";
	    		}
	    	}
	    	laststatus=err+status;
    	}
    	return laststatus;
	}
}
