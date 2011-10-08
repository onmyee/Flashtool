package flashsystem;

import java.util.Iterator;

import org.system.PropertiesFile;

public class BundleTrans {

	static PropertiesFile trans = new PropertiesFile("","./x10flasher_lib/bundletranslate.properties");

	static String getInternal(String external) {
		Iterator<Object> i = trans.keySet().iterator();
		if (external.toLowerCase().contains("amss_fs")) return trans.getProperty("amss_fs.");
		if (external.toLowerCase().contains("amss_") || external.toLowerCase().contains("amss.")) return trans.getProperty("amss.");
		while (i.hasNext()) {
			String key = (String)i.next();
			if ( external.toLowerCase().startsWith(key.toLowerCase()))
				return trans.getProperty(key);
		}
		return "";
	}

}
