package linuxlib;

public class LinuxUsbDevice {

	String vid="";
	String pid="";
	
	public LinuxUsbDevice(String vid, String pid) {
		this.vid = vid;
		this.pid = pid;
	}
	
	public String getIdVendor() {
		return vid;
	}
	
	public String getIdProduct() {
		return pid;
	}
}
