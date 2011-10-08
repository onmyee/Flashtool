package org.plugins;

import java.util.Enumeration;

public interface  PluginInterface {

	public String getName();
	public void run() throws Exception;
	public Enumeration getCompatibleVersions();

}
