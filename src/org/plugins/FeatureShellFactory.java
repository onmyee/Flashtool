package org.plugins;

import java.io.File;

import org.system.OS;
import org.system.Shell;

public class FeatureShellFactory {

	String _shelldir;
	
	public FeatureShellFactory(String shelldir) {
		_shelldir = shelldir;
	}
	
	public Shell createShell(String name) {
		try {
			Shell s = new Shell(new File(_shelldir+OS.getFileSeparator()+name));
			return s;
		}
		catch (Exception e) {
			return null;
		}
	}
}
