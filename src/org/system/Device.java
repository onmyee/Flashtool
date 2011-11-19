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
    
    public static String getConnectedDevice() {
    	String result = "none";
    	HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String id = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (id.contains("VID_0FCE") && !result.contains("MI")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		result = id + ": Not OK";
	            	else
	            		result = id + ": OK";
	            	break;
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return result.trim();
    }
    
    public static String getStatus(String device) {
    	String err="";
    	String status = "none";
    	if (!device.equals("none")) {
    		if (device.contains("Not OK"))
    			err = "Err";
    		String type = device.substring(17,18);
    		if (type.equals("6")) status="adb";
    		if (type.equals("E")) status="normal";
    		if (type.equals("A")) status="flash";
    		if (type.equals("0")) status="fastboot";
    	}
    	return err+status;
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
    	String DevicePath=getConnectedDevice();
    	String status = getStatus(DevicePath);
    	if (status.equals("Errflash")) return "ErrDriverError";
    	if (!status.equals("flash")) return "ErrNotPlugged";
    	return DevicePath.substring(0,DevicePath.indexOf(":"));
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
    	String result = getConnectedDevice();
	    MyLogger.getLogger().info(result);
    }    

}