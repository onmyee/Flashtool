package win32lib;

import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.Library;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

//-------------------------------------------------------------------------------------------------------------------
/** 
 * Declare a Java interface that holds the native "hid.dll" library methods by extending the W32API interface. 
 */
public interface Hid extends Library {

    byte HID_DEVICE_SUCCESS             = 0x00;
    byte HID_DEVICE_NOT_FOUND           = 0x01;
    byte HID_DEVICE_NOT_OPENED          = 0x02;
    byte HID_DEVICE_ALREADY_OPENED      = 0x03;
    byte HID_DEVICE_TRANSFER_TIMEOUT    = 0x04;
    byte HID_DEVICE_TRANSFER_FAILED     = 0x05;
    byte HID_DEVICE_CANNOT_GET_HID_INFO = 0x06;
    byte HID_DEVICE_HANDLE_ERROR        = 0x07;
    byte HID_DEVICE_INVALID_BUFFER_SIZE = 0x08;
 
    public static class HIDD_ATTRIBUTES extends Structure {
        public int   Size;
        public short VendorID;
        public short ProductID;
        public short VersionNumber;
    }
 
    public static class HIDP_CAPS extends Structure {
        public short Usage;
        public short UsagePage;
        public short InputReportByteLength;
        public short OutputReportByteLength;
        public short FeatureReportByteLength;
        public short Reserved[] = new short[17];
        public short NumberLinkConnectionNodes;
        public short NumberInputButtonCaps;
        public short NumberInputValueCaps;
        public short NumberInputDataIndices;
        public short NumberOutputButtonCaps;
        public short NumberOutputValueCaps;
        public short NumberOutputDateIndices;
        public short NumberFeatureButtonCaps;
        public short NumberFeatureValueCaps;
        public short NumberFeatureDataIndices;
    }

    void HidD_GetHidGuid(GUID Guid);

    int HidD_GetAttributes(WinNT.HANDLE Handle, HIDD_ATTRIBUTES HIDAttributes);

    int HidD_GetPreparsedData(WinNT.HANDLE Handle, IntByReference HIDPreparsedData);

    int HidP_GetCaps(int HIDPreparsedData, HIDP_CAPS HIDCapabilities);

    boolean HidD_SetFeature(WinNT.HANDLE HidDeviceObject, byte[] ReportBuffer, int ReportBufferLength);

    boolean HidD_GetFeature (WinNT.HANDLE HidDeviceObject, byte[] ReportBuffer, int ReportBufferLength);

}