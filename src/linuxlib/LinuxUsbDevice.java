package linuxlib;

public class LinuxUsbDevice {

	String vid="";
	String pid="";
	String serial = "";
	
	public LinuxUsbDevice(String vid, String pid, String serial) {
		this.vid = vid;
		this.pid = pid;
		this.serial = serial;
	}
	
	public String getIdVendor() {
		return vid;
	}
	
	public String getIdProduct() {
		return pid;
	}
	
	public String getSerial() {
		return serial;
	}

}
