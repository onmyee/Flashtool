package win32lib;

import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.Library;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/** 
 * Declare a Java interface that holds the native "setupapi.dll" library methods by extending the W32API interface. 
 */
public interface SetupApi extends Library {

	public static class HDEVINFO extends WinNT.HANDLE {}
    
    public static class SP_DRVINFO_DATA extends Structure {
    	public SP_DRVINFO_DATA() {
    		DriverDate = new WinBase.FILETIME();
    	}
        public int     cbSize;
        public int     DriverType;
        public int     Reserved;
        public char[]  Description = new char[255];
        public char[]  MfgName = new char[255];
        public char[]  ProviderName= new char[255];
        public WinBase.FILETIME  DriverDate;
        public int DriverVersion;
    }

    public static class SP_DEVINFO_DATA extends Structure {
        public SP_DEVINFO_DATA() {
            ClassGuid = new GUID();
        }
        public int  cbSize;
        public GUID ClassGuid;
        public int  DevInst;
        public int  Reserved;
    }

    public static class SP_DEVICE_INTERFACE_DATA extends Structure {
        public SP_DEVICE_INTERFACE_DATA() {
            InterfaceClassGuid = new GUID();
        }
        public int  cbSize;
        public GUID InterfaceClassGuid;
        public int  Flags;
        public int  Reserved;
    }
 
    public static class SP_DEVICE_INTERFACE_DETAIL_DATA extends Structure {
        public int cbSize;
        public char[] devicePath = new char[1];
        public SP_DEVICE_INTERFACE_DETAIL_DATA() {
            setAlignType(Structure.ALIGN_NONE);
        }        
    }
	
    public static int DIGCF_DEFAULT         = 0x00000001; 
    public static int DIGCF_PRESENT         = 0x00000002;
    public static int DIGCF_ALLCLASSES      = 0x00000004; 
    public static int DIGCF_PROFILE         = 0x00000008;
    public static int DIGCF_DEVICEINTERFACE = 0x00000010; 
    public static int SPDRP_DRIVER          = 0x00000009;
    public static int SPDRP_INSTALL_STATE   = 0x00000022;

    HDEVINFO SetupDiGetClassDevs(GUID Guid, String Enumerator, WinDef.HWND Parent, int Flags);

    int SetupDiEnumDeviceInfo(HDEVINFO DeviceInfoSet, int MemberIndex, SP_DEVINFO_DATA DeviceInfoData);

    int SetupDiEnumDeviceInterfaces(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData, GUID Guid, int MemberIndex, SP_DEVICE_INTERFACE_DATA DeviceInterfaceData);

    int SetupDiGetDeviceInterfaceDetail(HDEVINFO DeviceInfoSet, SP_DEVICE_INTERFACE_DATA DeviceInterfaceData, SP_DEVICE_INTERFACE_DETAIL_DATA DeviceInterfaceDetailData, int DeviceInterfaceDetailDataSize, IntByReference RequiredSize, SP_DEVINFO_DATA DeviceInfoData);

    int SetupDiDestroyDeviceInfoList(HDEVINFO  DeviceInfoSet);
    
    boolean SetupDiGetDeviceInstanceId(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData,	char[] DeviceId, int DeviceInstanceIdSize,IntByReference RequiredSize);
    
    boolean SetupDiClassNameFromGuid(GUID Guid, char[] ClassName, int ClassNameSize, IntByReference RequiredSize);
    
    boolean SetupDiGetSelectedDriver(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData, SP_DRVINFO_DATA DriverInfoData);

    boolean SetupDiClassGuidsFromName(String ClassName, GUID[] ClassGuidList, int ClassGuidListSize, IntByReference RequiredSize);
    
    boolean SetupDiGetDeviceRegistryProperty(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData, int Property, IntByReference PropertyRegDataType, byte[] PropertyBuffer, int PropertyBufferSize, IntByReference RequiredSize);

}
