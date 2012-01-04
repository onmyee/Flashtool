package org.system;

import gui.FlasherGUI;

import java.util.Enumeration;
import java.util.Iterator;

import linuxlib.JUsb;
import linuxlib.LinuxUsbDevice;

import org.adb.AdbUtility;
import org.adb.FastbootUtility;
import org.logger.MyLogger;
import com.sun.jna.platform.win32.WinBase;
import win32lib.JsetupAPi;
import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVINFO_DATA;

public class Device {	

	static DeviceIdent lastid = new DeviceIdent();
	static String laststatus = "";

	public static DeviceIdent getLastConnected() {
		DeviceIdent id = null;
		synchronized (lastid) {
			id = new DeviceIdent(lastid);
		}
		return id;
	}

	public static DeviceIdent getConnectedDevice() {
		DeviceIdent id;
		if (OS.getName().equals("windows")) id=getConnectedDeviceWin32();
		else id=getConnectedDeviceLinux();
		int count=0;
		while (!id.isDriverOk()) {
			try {
				Thread.sleep(200);
			}
			catch (Exception e) {
			}
			if (OS.getName().equals("windows")) id=getConnectedDeviceWin32();
			else id=getConnectedDeviceLinux();
			count++;
			if (count==5) break;
		}
		return id;
	}
	
    public static DeviceIdent getConnectedDeviceWin32() {
    	DeviceIdent id = new DeviceIdent();
    	HDEVINFO hDevInfo = JsetupAPi.getHandleForConnectedInterfaces();
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
	            	id.addDevPath(JsetupAPi.getDevicePath(hDevInfo, DeviceInfoData));
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
    	synchronized (lastid) {
    		lastid=id;
    	}
    	return id;
    }

    public static DeviceIdent getConnectedDeviceLinux() {
    	boolean notchanged=false;
    	DeviceIdent id = new DeviceIdent();
    	try {
    	Iterator<LinuxUsbDevice> i = JUsb.getConnectedDevices().iterator();
    	while (i.hasNext()) {
    		LinuxUsbDevice d = i.next();
    		id.addDevId(d.getIdVendor(),d.getIdProduct(),d.getSerial());
            if (lastid!=null)
            if (lastid.getIds().contains(d.getIdProduct())) {
            	notchanged=true;
            	break;
            }
        }
        synchronized (lastid) {
        	lastid=id;
        }
        if (notchanged) return getLastConnected();
    	}
    	catch (UnsatisfiedLinkError e) {
    		MyLogger.getLogger().error("libusb-1.0 is not installed");
    		MyLogger.getLogger().error(e.getMessage());
    	}
    	catch (NoClassDefFoundError e1) {
    	}
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

    public static void clean() {
    	if (OS.getName().equals("linux")) JUsb.clean(); 
    }
}