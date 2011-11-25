package org.system;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

import org.adb.AdbUtility;
import org.adb.FastbootUtility;

public class DeviceIdent {

	private String pid;
	private String vid;
	private Properties devid;
	
	public DeviceIdent() {
		pid="";
		vid="";
		devid=new Properties();
	}
	public void addDevId(String device) {
		String tmpvid=device.substring(device.indexOf("VID_"),device.indexOf("VID_")+12);
		vid=tmpvid.substring(4,tmpvid.indexOf("&"));
		String tmppid=device.substring(device.indexOf("PID_"),device.indexOf("PID_")+12);
		int endindex = tmppid.indexOf("\\");
		if (endindex == -1) endindex = tmppid.indexOf("&");
		pid=tmppid.substring(4,endindex);
		devid.setProperty(device, Boolean.toString(true));
	}

	public void setDriverOk(String device,boolean status) {
		devid.setProperty(device, Boolean.toString(status));
	}

	public String getPid() {
		return pid;
	}

	public String getVid() {
		return vid;
	}
	
	public boolean isDriverOk() {
		Enumeration e = devid.elements();
		while (e.hasMoreElements()) {
			String value = (String)e.nextElement();
			boolean b = Boolean.parseBoolean(value);
			if (!b) return false;
		}
		return true;
	}
	
	public String getDeviceId() {
		Enumeration e = devid.keys();
		while (e.hasMoreElements()) {
			String value = (String)e.nextElement();
			if (!value.contains("MI")) return value;
		}
		return "none";
	}

	public String getStatus() {
		String device = getDeviceId();
    	String status = "none";
	    if (!device.equals("none")) {
	    	if (getPid().equals("ADDE")) status="flash";
	    	else
	    		if (AdbUtility.getDevices().hasMoreElements())
	    			status="adb";
	    		else
	    			if (FastbootUtility.getDevices().hasMoreElements())
	    				status="fastboot";
	    		else {
	    			if (getPid().startsWith("0") || getPid().startsWith("E")) 
	    				status="normal";
	    			else
	    				status="mtp";
	    		}
	    }
    	return status;
	}
}
