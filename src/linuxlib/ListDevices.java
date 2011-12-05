package linuxlib;

import java.util.List;

import se.marell.libusb.UsbDevice;
import se.marell.libusb.UsbSystem;

public class ListDevices implements UsbSystem.UsbDeviceVisitor {
    public List<UsbDevice> visitDevices(List<UsbDevice> allDevices) {
    	return allDevices;
    }
}
