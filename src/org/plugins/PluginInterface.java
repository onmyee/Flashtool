package org.plugins;

import java.util.Enumeration;

import javax.swing.JMenu;

public interface  PluginInterface {

	public void setWorkdir(String workdir);
	public String getName();
	public void run() throws Exception;
	public Enumeration<String> getCompatibleAndroidVersions();
	public Enumeration<String> getCompatibleKernelVersions();
	public Enumeration<String> getCompatibleDevices();
	public boolean isRootNeeded();
	public void showAbout();
	public void setMenu(JMenu menu);

}
