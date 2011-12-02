package win32lib;

import win32lib.Hid.HIDD_ATTRIBUTES;

import org.logger.MyLogger;
import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVICE_INTERFACE_DATA;
import win32lib.SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA;
import win32lib.SetupApi.SP_DEVINFO_DATA;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class JsetupAPi {
	
	static SetupApi setupapi = (SetupApi) Native.loadLibrary("setupapi", SetupApi.class, W32APIOptions.UNICODE_OPTIONS);
    public static GUID USBGuid = new GUID();
    
    static {
	    USBGuid.Data1=0xA5DCBF10;
	    USBGuid.Data2=0x6530;
	    USBGuid.Data3=0x11D2;
	    USBGuid.Data4=new byte[8];
	    USBGuid.Data4[0]=(byte)0x90;
	    USBGuid.Data4[1]=(byte)0x1F;
	    USBGuid.Data4[2]=(byte)0x00; 
	    USBGuid.Data4[3]=(byte)0xC0; 
	    USBGuid.Data4[4]=(byte)0x4F; 
	    USBGuid.Data4[5]=(byte)0xB9; 
	    USBGuid.Data4[6]=(byte)0x51; 
	    USBGuid.Data4[7]=(byte)0xED;
    }
	
	public static String getClassName(GUID guid) {
		char[] ClassName = new char[100];
		boolean result = setupapi.SetupDiClassNameFromGuid(guid, ClassName, 100, null);
		if (result) {
			String name = new String();
			for (int i=0;i<100;i++) {
				if (ClassName[i]!=0)
					name = name+ClassName[i];
			}
			return name;
		}
		else {
			MyLogger.getLogger().error("Error calling SetupDiClassNameFromGuid");
		}
		return "";
	}

	public static GUID getGUID(String classname) {
		GUID[] ClassGuidList= new GUID[100];
		IntByReference size = new IntByReference();
		boolean result = setupapi.SetupDiClassGuidsFromName(classname, ClassGuidList, 100, size);
		if (result && (size.getValue()==1)) {
			return ClassGuidList[0];
		}
		else {
			MyLogger.getLogger().error("Error calling SetupDiClassNameFromGuid for "+classname);
		}
		return null;
	}

	public static void destroyHandle(HDEVINFO hDevInfo) {
		setupapi.SetupDiDestroyDeviceInfoList(hDevInfo);
	}
	
	public static SP_DEVINFO_DATA enumDevInfo(HDEVINFO hDevInfo, int index) {
        SP_DEVINFO_DATA DeviceInfoData = new SP_DEVINFO_DATA();
        DeviceInfoData.cbSize = DeviceInfoData.size();
		int result = setupapi.SetupDiEnumDeviceInfo(hDevInfo, index, DeviceInfoData);
		if (result == 0) DeviceInfoData=null;
		return DeviceInfoData;
	}
	
	public static HDEVINFO getHandleForAllClasses() {
		return setupapi.SetupDiGetClassDevs(null, null, null, SetupApi.DIGCF_PRESENT|SetupApi.DIGCF_ALLCLASSES);
	}

	public static HDEVINFO getHandleForInterfaces(GUID Guid) {
		return setupapi.SetupDiGetClassDevs(Guid, null, null, SetupApi.DIGCF_PRESENT|SetupApi.DIGCF_DEVICEINTERFACE);
	}

	public static HDEVINFO getHandleForConnectedClasses() {
		return setupapi.SetupDiGetClassDevs(USBGuid, null, null, SetupApi.DIGCF_PRESENT|SetupApi.DIGCF_DEVICEINTERFACE);
	}
	
	public static boolean isInstalled(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData) {
		byte[] res = {'a','a','a','a'};
		setupapi.SetupDiGetDeviceRegistryProperty(DeviceInfoSet, DeviceInfoData, SetupApi.SPDRP_INSTALL_STATE,null,res,4,null);
		return (res[0]==0);
	}
	
	public static String getDevId(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData) {
		char[] DeviceId = new char[100];
		boolean result = setupapi.SetupDiGetDeviceInstanceId(DeviceInfoSet,	DeviceInfoData,	DeviceId, 100,null);
		if (result) {
			String name = new String();
			for (int i=0;i<100;i++) {
				if (DeviceId[i]!=0)
					name = name+DeviceId[i];
			}
			return name;
		}
		return "";
	}

	public static String getDevicePath(HDEVINFO hDevInfo, SP_DEVINFO_DATA DeviceInfoData) {
		String devpath = "";
        SP_DEVICE_INTERFACE_DATA DeviceInterfaceData = new SP_DEVICE_INTERFACE_DATA();
        DeviceInterfaceData.cbSize = DeviceInterfaceData.size();
        /* Query the device using the index to get the interface data */
        int index=0;
        do {
        	int result = setupapi.SetupDiEnumDeviceInterfaces(hDevInfo, DeviceInfoData, USBGuid,index, DeviceInterfaceData);
        	if (result == 0) {
        		break;        		
        	}
    		/* A successful query was made, use it to get the detailed data of the device */
    	    IntByReference reqlength = new IntByReference();
    	    /* Obtain the length of the detailed data structure, and then allocate space and retrieve it */
    	    result = setupapi.SetupDiGetDeviceInterfaceDetail(hDevInfo, DeviceInterfaceData, null, 0, reqlength, null);
    	    // Create SP_DEVICE_INTERFACE_DETAIL_DATA structure and set appropriate length for device Path */
    	    SP_DEVICE_INTERFACE_DETAIL_DATA DeviceInterfaceDetailData      = new SP_DEVICE_INTERFACE_DETAIL_DATA();
    	    SP_DEVICE_INTERFACE_DETAIL_DATA DeviceInterfaceDetailDataDummy = new SP_DEVICE_INTERFACE_DETAIL_DATA();
    	    DeviceInterfaceDetailData.cbSize = DeviceInterfaceDetailDataDummy.size();
    	    DeviceInterfaceDetailData.devicePath = new char[reqlength.getValue()];
    	    result = setupapi.SetupDiGetDeviceInterfaceDetail(hDevInfo, DeviceInterfaceData, DeviceInterfaceDetailData, reqlength.getValue(), reqlength, null);
    	    devpath = Native.toString(DeviceInterfaceDetailData.devicePath);
            index++;
        } while (true);
        return devpath;
	}

	private static String getSystemError(int code) {
        Kernel32 lib = Kernel32.INSTANCE;
        PointerByReference pref = new PointerByReference();
        lib.FormatMessage(
            WinBase.FORMAT_MESSAGE_ALLOCATE_BUFFER | WinBase.FORMAT_MESSAGE_FROM_SYSTEM | WinBase.FORMAT_MESSAGE_IGNORE_INSERTS, 
            null, 
            code, 
            0, 
            pref, 
            0, 
            null);
        String s = pref.getValue().getString(0, !Boolean.getBoolean("w32.ascii"));
        lib.LocalFree(pref.getValue());
        return s;
    } 

}