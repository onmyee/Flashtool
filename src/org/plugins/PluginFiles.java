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
			MyLogger.getLogger().error("Error sending "+file+" to device");
			return false;
		}
	}

	public boolean push(String folder, String file) {
		try {
			AdbUtility.push(_filedir+OS.getFileSeparator()+folder+OS.getFileSeparator()+file, GlobalConfig.getProperty("deviceworkdir"));
			return true;
		}
		catch (Exception e) {
			MyLogger.getLogger().error("Error sending "+folder+OS.getFileSeparator()+file+" to device");
			return false;
		}
	}
	
	public boolean pull(String file) {
		try {
			AdbUtility.pull(file, _filedir+OS.getFileSeparator()+".");
			return true;
		}
		catch (Exception e) {
			MyLogger.getLogger().error("Error sending "+file+" to device");
			return false;			
		}
	}

	public boolean pullWithRename(String file, String newname) {
		try {
			AdbUtility.pull(file, _filedir+OS.getFileSeparator()+newname);
			return true;
		}
		catch (Exception e) {
			MyLogger.getLogger().error("Error sending "+file+" to device");
			return false;			
		}
	}

	public String getFileDir() {
		return _filedir;
	}

	public String getFile(String file) {
		return _filedir+OS.getFileSeparator()+file;
	}
	
	public String getFile(String folder, String file) {
		return _filedir+OS.getFileSeparator()+folder+OS.getFileSeparator()+file;
	}
}
