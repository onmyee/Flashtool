package org.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import java.util.Set;

public class PropertiesFile {

	String rname;
	String fname;
	Properties props;
	
	public PropertiesFile() {	
		props=new Properties();
	}
	
	public void setProperties(Properties p) {
		props=p;
	}
	
	public void open(String arname, String afname) {
		rname = arname;
		fname = afname;
        try {
        	props = new Properties();
        	Reader in = new InputStreamReader(new FileInputStream(fname), "UTF-8"); 
        	props.load(in);
        }
        catch (Exception e) {
        	try {
        		props = new Properties();
        		Reader in = new InputStreamReader(PropertiesFile.class.getClassLoader().getResourceAsStream(rname), "UTF-8");
        		props.load(in);
        	}
        	catch (Exception e1) {
        	}
        }		
	}
	
	public PropertiesFile(String arname, String afname) {
		open(arname, afname);
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	public Set<Object> keySet() {
		return props.keySet();
	}
	
	public void setProperty(String key, String value) {
		props.setProperty(key, value);
	}
	
	public void write(String filename,String encoding) {
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(filename), encoding);
			props.store(out, null);
			out.flush();
			out.close();
		}
		catch (Exception e) {
		}		
	}
	
	public void write(String encoding) {
		write(fname,encoding);
	}
	
	public Properties getProperties() {
		return props;
	}
}