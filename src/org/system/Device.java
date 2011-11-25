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
    
	private static String lastdevice="";
	private static String laststatus="";
	
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
	            id = new DeviceIdent(JsetupAPi.getDevId(hDevInfo, DeviceInfoData));
	            if (id.getPid().equals("0FCE") && id.getDeviceId().contains("MI")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		id.setDriverOk(false);
	            	else
	            		id.setDriverOk(true);
	            	break;
	            }
	            else if (id.getPid().equals("0FCE")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		id.setDriverOk(false);
	            	else
	            		id.setDriverOk(true);
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return id;
    }

    public static String getDeviceIdAdbMode() {
    	String DevicePath="ErrNotPlugged";
        HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String result = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (result.contains("VID_0FCE&PID_612E")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		DevicePath="ErrDriverError";
	            	else DevicePath=result;
	            	break;
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return DevicePath;
    }

    public static String getDeviceIdFlashMode() {
    	DeviceIdent id=getConnectedDevice();
    	if (!id.isDriverOk()) return "ErrDriverError";
    	if (!id.getPid().equals("ADDE")) return "ErrNotPlugged";
    	return id.getDeviceId();
    }

    public static String getDeviceIdFastbootMode() {
    	String DevicePath="ErrNotPlugged";
        HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String result = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (result.contains("VID_0FCE&PID_0DDE")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		DevicePath="ErrDriverError";
	            	else DevicePath=result;
	            	break;
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return DevicePath;
    }

    public static void CheckAdbDrivers() {
    	MyLogger.getLogger().info("List of connected devices (Device Id) :");
    	MyLogger.getLogger().info("      - "+getConnectedDevice());
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