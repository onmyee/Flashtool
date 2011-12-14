package org.system;

import java.util.Enumeration;
import java.util.Properties;
import org.adb.AdbUtility;
import org.adb.FastbootUtility;

public class DeviceIdent {

	private String pid;
	private String vid;
	private String devicepath;
	private Properties devid;
	private int maxsize=0;
	
	public DeviceIdent() {
		pid="";
		vid="";
		devicepath="";
		devid=new Properties();
	}
	
	public DeviceIdent(DeviceIdent id) {
		devid=new Properties();
		Enumeration<Object> e = id.getIds().keys();
		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			addDevId(key);
			devid.setProperty(key, id.getIds().getProperty(key));
		}
		addDevPath(id.getDevPath());
	}

	public void addDevPath(String path) {
		devicepath=path;
	}
	
	public String getDevPath() {
		return devicepath;
	}
	
	public void addDevId(String device) {
		if (device.length()>maxsize) maxsize=device.length();
		String tmpvid=device.substring(device.indexOf("VID_"),device.indexOf("VID_")+12);
		vid=tmpvid.substring(4,tmpvid.indexOf("&"));
		String tmppid=device.substring(device.indexOf("PID_"),device.indexOf("PID_")+9);
		int endindex = tmppid.indexOf("\\");
		if (endindex == -1) endindex = tmppid.indexOf("&");
		pid=tmppid.substring(4,endindex);
		devid.setProperty(device, Boolean.toString(true));
	}

	public void addDevId(String vendor, String product) {
		vid = vendor;
		pid = product;
		devid.setProperty("VID_"+vendor+"&"+"PID_"+product+"\\", "true");
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
	    	else {
	    		int counter = 0;
	    		while (!AdbUtility.getDevices().hasMoreElements() && counter<201) {
	    			try {
	    				Thread.sleep(10);
	    				counter+=1;
	    			}
	    			catch (Exception e) {}
	    		}
	    		if (AdbUtility.getDevices().hasMoreElements())
	    			status="adb";
	    		else
	    			if (FastbootUtility.getDevices().hasMoreElements())
	    				status="fastboot";
	    		else {
	    			if (getPid().startsWith("0") || getPid().startsWith("E")) 
	    				status="normal";
	    			else if (getPid().startsWith("5"))
	    				status="mtp";
	    			else status = "unknown";
	    		}
	    	}
	    }
    	return status;
	}
	
	public Properties getIds() {
		return devid;
	}
	
	public int getMaxSize() {
		return maxsize;
	}
}
