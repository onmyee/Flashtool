package flashsystem;

import java.util.Iterator;

import org.system.PropertiesFile;

public class BundleTrans {

	static PropertiesFile trans = new PropertiesFile("","./x10flasher_lib/bundletranslate.properties");

	static String getInternal(String external) {
		Iterator<Object> i = trans.keySet().iterator();
		while (i.hasNext()) {
			String key = (String)i.next();
			if ( external.toLowerCase().startsWith(key.toLowerCase()))
				return trans.getProperty(key);
		}
		return "";
	}

}
