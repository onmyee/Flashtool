package org.plugins;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JMenu;

import org.system.OS;
import org.system.PropertiesFile;

public class PluginDefaults {

	static protected String fsep = OS.getFileSeparator();
	static protected String _workdir;
	static protected PropertiesFile featureProperties;
	static protected FeatureShellFactory sfactory;
	static protected PluginFiles files;
	static protected JMenu menu;

	public void setWorkdir(String workdir) {
		_workdir = workdir;
		sfactory = new FeatureShellFactory(workdir+fsep+"shells");
		files = new PluginFiles(workdir+fsep+"files");
	}

	public Enumeration<String> getCompatibleAndroidVersions() {
		Vector<String> v = new Vector<String>();
		String[] list = getProperty("androidversions").split(",");
		for (int i = 0;i<list.length;i++)
			v.add(list[i]);
		return v.elements();
	}

	public Enumeration<String> getCompatibleKernelVersions() {
		Vector<String> v = new Vector<String>();
		String[] list = getProperty("kernelversions").split(",");
		for (int i = 0;i<list.length;i++)
			v.add(list[i]);
		return v.elements();
	}

	public Enumeration<String> getCompatibleDevices() {
		Vector<String> v = new Vector<String>();
		String[] list = getProperty("compatibledevices").split(",");
		for (int i = 0;i<list.length;i++)
			v.add(list[i]);
		return v.elements();
	}

	public static String getProperty(String property) {
		if (featureProperties==null)
			featureProperties = new PropertiesFile("",_workdir+fsep+"feature.properties");
		return featureProperties.getProperty(property);
	}

	public void setMenu(JMenu pmenu) {
		menu=pmenu;
	}
}
