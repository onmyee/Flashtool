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
        return id;
    }

    public static void CheckAdbDrivers() {
    	MyLogger.getLogger().info("List of connected devices (Device Id) :");
    	DeviceIdent id=getLastConnected();
    	String driverstatus;
    	if (id.isDriverOk()) driverstatus = "Installed";
    	else driverstatus = "Not Installed";
    	MyLogger.getLogger().info("      - "+id.getDeviceId()+". Driver status : "+driverstatus);
	    MyLogger.getLogger().info("List of ADB devices :");
	    Enumeration<String> e = AdbUtility.getDevices();
	    while (e.hasMoreElements()) {
	    	MyLogger.getLogger().info("      - "+e.nextElement());
	    }
	    MyLogger.getLogger().info("List of fastboot devices :");
	    Enumeration<String> e2 = FastbootUtility.getDevices();
	    while (e2.hasMoreElements()) {
	    	MyLogger.getLogger().info("      - "+e2.nextElement());
	    }
    }

}