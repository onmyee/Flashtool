package win32lib;

import org.logger.MyLogger;

import win32lib.SetupApi.HDEVINFO;
import win32lib.SetupApi.SP_DEVINFO_DATA;

import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.ptr.IntByReference;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class JsetupAPi {
	static SetupApi setupapi = (SetupApi) Native.loadLibrary("setupapi", SetupApi.class, W32APIOptions.UNICODE_OPTIONS);
	
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

	public static GUID[] getGUIDs(String classname) {
		GUID[] ClassGuidList= new GUID[100];
		IntByReference size = new IntByReference();
		boolean result = setupapi.SetupDiClassGuidsFromName(classname, ClassGuidList, 100, size);
		if (result) {
			return ClassGuidList;
		}
		else {
			MyLogger.getLogger().error("Error calling SetupDiClassNameFromGuid");
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
		return setupapi.SetupDiGetClassDevs(null, null, null, SetupApi.DIGCF_ALLCLASSES);
	}

	public static HDEVINFO getHandleForConnectedClasses() {
		GUID guid = getGUIDs("USB")[0];
		return setupapi.SetupDiGetClassDevs(guid, "USB", null, SetupApi.DIGCF_PRESENT|SetupApi.DIGCF_ALLCLASSES);
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
}