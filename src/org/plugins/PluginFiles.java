package org.plugins;

import org.adb.AdbUtility;
import org.logger.MyLogger;
import org.system.GlobalConfig;
import org.system.OS;

public class PluginFiles {

	String _filedir;
	
	public PluginFiles(String filedir) {
		_filedir = filedir;
	}
	
	public boolean push(String file) {
		try {
			AdbUtility.push(_filedir+OS.getFileSeparator()+file, GlobalConfig.getProperty("deviceworkdir"));
			return true;
		}
		catch (Exception e) {
			MyLogger.error("Error sending "+file+" to device");
			return false;
		}
	}
	
	public String getFileDir() {
		return _filedir;
	}

}
