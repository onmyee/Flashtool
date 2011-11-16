package org.system;

import java.util.Enumeration;

import org.logger.MyLogger;
import com.sun.jna.platform.win32.WinBase;
import win32lib.JsetupAPi;
import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVINFO_DATA;

public class Device {
	
	public static String longestSubstring(String str1, String str2) {
		StringBuilder sb = new StringBuilder();
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
		return "";
		// ignore case
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();
		// java initializes them already with 0
		int[][] num = new int[str1.length()][str2.length()];
		int maxlen = 0;
		int lastSubsBegin = 0;
		for (int i = 0; i < str1.length(); i++) {
		for (int j = 0; j < str2.length(); j++) {
		if (str1.charAt(i) == str2.charAt(j)) {
		if ((i == 0) || (j == 0))
		num[i][j] = 1;
		else
		num[i][j] = 1 + num[i - 1][j - 1];
		if (num[i][j] > maxlen) {
		maxlen = num[i][j];
		// generate substring from str1 => i
		int thisSubsBegin = i - num[i][j] + 1;
		if (lastSubsBegin == thisSubsBegin) {
		//if the current LCS is the same as the last time this block ran
		sb.append(str1.charAt(i));
		} else {
		//this block resets the string builder if a different LCS is found
		lastSubsBegin = thisSubsBegin;
		sb = new StringBuilder();
		sb.append(str1.substring(lastSubsBegin, i + 1));
		}
		}
		}
		}}
		return sb.toString().toUpperCase();
		}

    public static boolean previouslyConnected() {
    	boolean retcode=false;
        HDEVINFO hDevInfo = JsetupAPi.getHandleForAllClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
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
        	MyLogger.getLogger().error("Cannot have device list");
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
    
    public static String getStatus() {
    	String device="none";
    	String result;
    	HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedClasses();
        if (hDevInfo.equals(WinBase.INVALID_HANDLE_VALUE)) {
        	MyLogger.getLogger().error("Cannot have device list");
        }
        else {
        	SP_DEVINFO_DATA DeviceInfoData;
        	int index = 0;
	        do {
	        	DeviceInfoData = JsetupAPi.enumDevInfo(hDevInfo, index);
	            result = JsetupAPi.getDevId(hDevInfo, DeviceInfoData);
	            if (result.contains("VID_0FCE")) {
	            	device="normal";
		            Enumeration<String> adb = DriversConfig.getAdb();
		            while (adb.hasMoreElements()) {
			            if (result.contains(adb.nextElement())) {
			            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData)) {
			            		device="Erradb";
			            	}
			            	else {
			            		device="adb";
			            	}
			            	break;
			            }
		            }
		            if (device.contains("adb")) break;
		            Enumeration<String> fastboot = DriversConfig.getFastboot();
		            while (fastboot.hasMoreElements()) {
			            if (result.contains(fastboot.nextElement())) {
			            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData)) {
			            		device="Errfastboot";
			            	}
			            	else {
			            		device="fastboot";
			            	}
			            	break;
			            }
		            }
		            if (device.contains("fastboot")) break;
		            Enumeration<String> flash = DriversConfig.getFlash();
		            while (flash.hasMoreElements()) {
			            if (result.contains(flash.nextElement())) {
			            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData)) {
			            		device="Errflash";
			            	}
			            	else {
			            		device="flash";
			            	}
			            	break;
			            }
		            }
		            if (device.contains("flash")) break;
		            Enumeration<String> normal = DriversConfig.getFlash();
		            while (flash.hasMoreElements()) {
			            if (result.contains(flash.nextElement())) {
			            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData)) {
			            		device="Errnormal";
			            	}
			            	else {
			            		device="normal";
			            	}
			            	break;
			            }
		            }
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
        return device;
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
	            if (result.contains("USB\\VID_0FCE")) {
	            	if (!JsetupAPi.isInstalled(hDevInfo, DeviceInfoData))
	            		MyLogger.getLogger().error(result+" : Driver Error");
	            	else MyLogger.getLogger().info(result+" : OK");
	            }
	            index++;
	        } while (DeviceInfoData!=null);
	        JsetupAPi.destroyHandle(hDevInfo);
        }
    }    
}