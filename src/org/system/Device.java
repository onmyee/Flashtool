package org.system;

import org.logger.MyLogger;
import com.sun.jna.platform.win32.WinBase;
import win32lib.JsetupAPi;
import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVINFO_DATA;

public class Device {

    public static boolean previouslyConnected() {
    	boolean retcode=false;
        HDEVINFO hDevInfo = JsetupAPi.getHandleForAllClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.error("Cannot have device list");
        }
        else {
        	int index = 0;
	        do {
	        	SP_DEVINFO_DATA DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            if (DeviceInfoData == null) {
	                break;
	            }
	            String DevicePath = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (DevicePath.contains("VID_0FCE&PID_ADDE")) {
	            	retcode = true;
	            }
	            index++;
	        } while (!retcode);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return retcode;
    }

    public static boolean isInstalled() {
    	boolean retcode=false;
        HDEVINFO hDevInfo = JsetupAPi.getHandleForAllClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.error("Cannot have device list");
        }
        else {
        	int index = 0;
	        do {
	        	SP_DEVINFO_DATA DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            if (DeviceInfoData == null) {
	                break;
	            }
	            String DevicePath = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (DevicePath.contains("VID_0FCE&PID_ADDE")) {
	            	retcode = JsetupAPi.isInstalled(hDevInfo, DeviceInfoData);
	            }
	            index++;
	        } while (!retcode);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return retcode;
    }
    	
    public static String getDeviceIdFlashMode() {
    	String DevicePath="ErrNotPlugged";
        HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String result = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (result.contains("VID_0FCE&PID_ADDE")) {
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

    public static String getDeviceIdFastbootMode() {
    	String DevicePath="ErrNotPlugged";
        HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.error("Cannot have device list");
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
        HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            String result = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (result.contains("USB\\VID_0FCE")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		MyLogger.error(result+" : Driver Error");
	            	else MyLogger.info(result+" : OK");
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
    }    
}