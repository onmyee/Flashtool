package org.system;

import java.util.Enumeration;
import org.adb.AdbUtility;
import org.adb.FastbootUtility;
import org.logger.MyLogger;
import com.sun.jna.platform.win32.WinBase;
import win32lib.JsetupAPi;
import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVINFO_DATA;

public class Device {	

	static DeviceIdent lastid;
	static boolean idmodlock=false;
	static boolean idquerylock=false;

	public static DeviceIdent getLastConnected() {
		while (idmodlock);
		idquerylock=true;
		DeviceIdent id = new DeviceIdent(lastid);
		idquerylock=false;
		return id;
	}

    public static DeviceIdent getConnectedDevice() {
    	boolean notchanged=false;
    	DeviceIdent id = new DeviceIdent();
    	HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String devid = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (lastid!=null)
	            if (lastid.getIds().contains(devid)) {
	            	notchanged=true;
	            	break;
	            }
	            if (devid.contains("VID_0FCE")) {
	            	id.addDevId(devid);
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		id.setDriverOk(devid,false);
	            	else
	            		id.setDriverOk(devid,true);
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        while (idquerylock);
        idmodlock=true;
        lastid=id;
        idmodlock=false;
        if (notchanged) return getLastConnected();
        return id;
    }

    public static void CheckAdbDrivers() {
    	MyLogger.getLogger().info("List of connected devices (Device Id) :");
    	DeviceIdent id=getLastConnected();
    	String driverstatus;
    	int maxsize = id.getMaxSize();
    	Enumeration e = id.getIds().keys();
    	while (e.hasMoreElements()) {
    		String dev = (String)e.nextElement();
    		String driver = id.getIds().getProperty(dev);
    		MyLogger.getLogger().info("      - "+String.format("%1$-" + maxsize + "s", dev)+"\tDriver installed : "+driver);
    	}
	    MyLogger.getLogger().info("List of ADB devices :");
	    Enumeration<String> e1 = AdbUtility.getDevices();
	    if (e1.hasMoreElements()) {
	    while (e1.hasMoreElements()) {
	    	MyLogger.getLogger().info("      - "+e1.nextElement());
	    }
	    }
	    else MyLogger.getLogger().info("      - none");
	    MyLogger.getLogger().info("List of fastboot devices :");
	    Enumeration<String> e2 = FastbootUtility.getDevices();
	    if (e1.hasMoreElements()) {
	    while (e2.hasMoreElements()) {
	    	MyLogger.getLogger().info("      - "+e2.nextElement());
	    }
	    }
	    else MyLogger.getLogger().info("      - none");
    }

}