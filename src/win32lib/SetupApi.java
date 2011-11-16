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

    public static class HDEVINFO extends WinNT.HANDLE {
    }
    
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
    };

    /** 
     * SP_DEVINFO_DATA structure defines a device instance that is a member of a device information set. 
     */
    public static class SP_DEVINFO_DATA extends Structure {
        public SP_DEVINFO_DATA() {
            ClassGuid = new GUID();
        }
        /** 
         * The size, in bytes, of the SP_DEVINFO_DATA structure. 
         */
        public int  cbSize;
        /** 
         * Device setup class GUID 
         */ 
        public GUID ClassGuid;
        /** 
         * Handle to the device instance (handle to the devnode) 
         */
        public int  DevInst;
        /** 
         * Reserved. For internal use only. 
         */
        public int  Reserved;
    }

    public static class SP_DEVICE_INTERFACE_DATA extends Structure {
        public SP_DEVICE_INTERFACE_DATA() {
            InterfaceClassGuid = new GUID();
        }
        /** 
         * The size, in bytes, of the SP_DEVICE_INTERFACE_DATA structure. 
         */
        public int  cbSize;
        /** 
         * The GUID for the class to which the device interface belongs. 
         */
        public GUID InterfaceClassGuid;
        /** 
         * Can be one or more of the following:
         *  SPINT_ACTIVE    - The interface is active (enabled).
         *  SPINT_DEFAULT   - The interface is the default interface for the device class.
         *  SPINT_REMOVED   - The interface is removed.  
         */
        public int  Flags;
        /** 
         * Reserved. Do not use. 
         */
        public int  Reserved;
    }
 
    //-------------------------------------------------------------------------------------------------------------------
    /** 
     * An SP_DEVICE_INTERFACE_DETAIL_DATA structure contains the path for a device interface. 
     */
    public static class SP_DEVICE_INTERFACE_DETAIL_DATA extends Structure {
        /** 
         * The size, in bytes, of the fixed portion of the SP_DEVICE_INTERFACE_DETAIL_DATA structure. 
         */
        public int cbSize;
        /** 
         * A NULL-terminated string that contains the device interface path. This path can be passed to Win32 functions CreateFile. 
         */
        public char[] devicePath = new char[1];
        public SP_DEVICE_INTERFACE_DETAIL_DATA() {
            setAlignType(Structure.ALIGN_NONE);
        }        
    }
	
	/** 
     * Load a library interface from the "setupapi.dll" shared library 
     */
//    Setupapi INSTANCE = (Setupapi) Native.loadLibrary("setupapi", Setupapi.class, W32APIOptions.UNICODE_OPTIONS);

   /** 
    * SetupDiGetClassDevs Flag constants.
    * 
    * Specifies control options that filter the device information elements that are added to the device information set. 
    */
    /** 
     * Return only the device that is associated with the system default device interface, if one is set, for the specified device interface classes. 
     */
    public static int DIGCF_DEFAULT         = 0x00000001; 
    /** 
     * Return only devices that are currently present in a system. 
     */
    static int DIGCF_PRESENT                = 0x00000002;
    /** 
     * Return a list of installed devices for all device setup classes or all device interface classes. 
     */
    public static int DIGCF_ALLCLASSES      = 0x00000004; 
    /** 
     * Return only devices that are a part of the current hardware profile. 
     */
    public static int DIGCF_PROFILE         = 0x00000008;
    /** 
     * Return devices that support device interfaces for the specified device interface classes. 
     * 
     * This flag must be set in the Flags parameter if the Enumerator parameter specifies a device instance ID. 
     */
    public static int DIGCF_DEVICEINTERFACE = 0x00000010; 
    
    public static int SPDRP_DRIVER = 0x00000009;

    public static int SPDRP_INSTALL_STATE = 0x00000022;

    /** 
     * Method returns a device information set that contains all devices of a specified class. 
     */
    HDEVINFO SetupDiGetClassDevs(GUID Guid, String Enumerator, WinDef.HWND Parent, int Flags);

    int SetupDiEnumDeviceInfo(HDEVINFO DeviceInfoSet, int MemberIndex, SP_DEVINFO_DATA DeviceInfoData);

    /** 
     * Method returns a context structure for a device interface element of a device information set. 
     * 
     * Each call returns information about one device interface.
     * The function can be called repeatedly to get information about several 
     * interfaces exposed by one or more devices. 
     */
    int SetupDiEnumDeviceInterfaces(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData, GUID Guid, int MemberIndex, SP_DEVICE_INTERFACE_DATA DeviceInterfaceData);
    /** 
     * Method returns details about a particular device interface. 
     */
    int SetupDiGetDeviceInterfaceDetail(HDEVINFO DeviceInfoSet, SP_DEVICE_INTERFACE_DATA DeviceInterfaceData, SP_DEVICE_INTERFACE_DETAIL_DATA DeviceInterfaceDetailData, int DeviceInterfaceDetailDataSize, IntByReference RequiredSize, SP_DEVINFO_DATA DeviceInfoData);
    /** 
     * Method destroys a device information set and frees all associated memory. 
     */
    int SetupDiDestroyDeviceInfoList(HDEVINFO  DeviceInfoSet);
    
    boolean SetupDiGetDeviceInstanceId(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData,	char[] DeviceId, int DeviceInstanceIdSize,IntByReference RequiredSize);
    
    boolean SetupDiClassNameFromGuid(GUID Guid, char[] ClassName, int ClassNameSize, IntByReference RequiredSize);
    
    boolean SetupDiGetSelectedDriver(HDEVINFO DeviceInfoSet, SP_DEVINFO_DATA DeviceInfoData, SP_DRVINFO_DATA DriverInfoData);

    boolean SetupDiGetDeviceRegistryProperty(
    		  HDEVINFO DeviceInfoSet,
    		  SP_DEVINFO_DATA DeviceInfoData,
    		  int Property,
    		  IntByReference PropertyRegDataType,
    		  byte[] PropertyBuffer,
    		  int PropertyBufferSize,
    		  IntByReference RequiredSize
    		);
    boolean SetupDiClassGuidsFromName(
    		  String ClassName,
    		  GUID[] ClassGuidList,
    		  int ClassGuidListSize,
    		  IntByReference RequiredSize
    		);
}
