package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.adb.AdbUtility;
import org.system.OS;
import org.system.PropertiesFile;

public class X10Apps {

	private static String fsep = OS.getFileSeparator();
	private PropertiesFile deviceList=new PropertiesFile("","."+fsep+"custom"+fsep+"clean"+fsep+"x10list.properties");
	private PropertiesFile customList=new PropertiesFile("","."+fsep+"custom"+fsep+"clean"+fsep+"customlist.properties");
	private Properties x10List = new Properties();
	private Properties safeList;
	private HashSet<String> currentList;
	private String currentProfile="";
	private Properties realnames = new Properties();
	private HashMap<String,PropertiesFile> Allsafelist = new HashMap<String,PropertiesFile>();

	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	private void copyToAppsSaved(File src) throws IOException {
		File dst = new File("./custom/apps_saved/"+src.getName());
		if (!dst.exists()) {
		    InputStream in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dst);
	
		    // Transfer bytes from in to out
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		}
	}
	
	public Properties deviceList() {
		return deviceList.getProperties();
	}

	public Properties customList() {
		return customList.getProperties();
	}

	public void addApk(File apk, String desc) {
		try {
			copyToAppsSaved(apk);
			customList.setProperty(apk.getName(), desc);
			customList.write("UTF-8");
			x10List.setProperty(apk.getName(), desc);
			realnames.setProperty(desc, apk.getName());
			rescan();
		}
		catch (Exception e) {
		}
	}
	
	public void modApk(String apkname, String desc) {
		if (customList.getProperties().containsKey(apkname)) {
			customList.setProperty(apkname,desc);
			customList.write("UTF-8");
		}
		if (deviceList.getProperties().containsKey(apkname)) {
			deviceList.setProperty(apkname,desc);
			deviceList.write("UTF-8");
		}
		x10List.setProperty(apkname,desc);
		realnames.setProperty(desc, apkname);
		rescan();
	}
	
	private void rescan() {
		File[] dirlist = (new File("."+fsep+"custom"+fsep+"clean")).listFiles();
		for (int i=0;i<dirlist.length;i++) {
			if (dirlist[i].getName().contains("safelist")) {
				String key = dirlist[i].getName().replace((CharSequence)"safelist", (CharSequence)"");
				key=key.replace((CharSequence)".properties", (CharSequence)"");
				if (!Allsafelist.containsKey(key.toLowerCase()))
					Allsafelist.put(key.toLowerCase(),new PropertiesFile("",dirlist[i].getPath()));
			}
		}
	}
	
	public String getCurrentProfile() {
		return currentProfile;
	}
	
	public X10Apps() {
		try {
			currentList = AdbUtility.listSysApps();
			Allsafelist.put("default",new PropertiesFile());
			Iterator i = deviceList.getProperties().keySet().iterator();
			while (i.hasNext()) {
				String key = (String)i.next();
				x10List.setProperty(key,deviceList.getProperty(key));
			}
			Iterator i1 = customList.getProperties().keySet().iterator();
			while (i1.hasNext()) {
				String key = (String)i1.next();
				x10List.setProperty(key,customList.getProperty(key));
			}
			rescan();
		}
		catch (Exception e) {
		}
	}
	
	public void setProfile(String profile) {
		currentProfile=profile;
		safeList = (Properties)Allsafelist.get(profile).getProperties().clone();
		fillSet();
		deviceList.write("UTF-8");
		customList.write("UTF-8");
	}

	public void saveProfile() {
		Allsafelist.get(currentProfile).setProperties(safeList);
		Allsafelist.get(currentProfile).write("UTF-8");
	}

	public void saveProfile(String name) {
		Allsafelist.get(currentProfile).setProperties(safeList);
		Allsafelist.get(currentProfile).write("."+fsep+"custom"+fsep+"clean"+fsep+"safelist"+name+".properties","UTF-8");
		rescan();
		setProfile(name.toLowerCase());
	}
	
	private void fillSet() {
		try {
			Iterator<String> i = currentList.iterator();
			while (i.hasNext()) {
				String apk = i.next();
				if (x10List.getProperty(apk)==null) {
					x10List.setProperty(apk,apk);
				}
				if (safeList.getProperty(apk)==null) {
					safeList.setProperty(apk,"unsafe");
				}
			}
			Enumeration<Object> e = safeList.keys();
			while (e.hasMoreElements()) {
				String apk = (String)e.nextElement();
				if (x10List.getProperty(apk)==null) {
					x10List.setProperty(apk,apk);
				}
			}
			Iterator<Object> i1 = x10List.keySet().iterator();
			while (i1.hasNext()) {
				String key = (String)i1.next();
				realnames.setProperty(x10List.getProperty(key), key);
			}
		}
		catch (Exception e) {
		}
	}
	
	public Set<String> getProfiles() {
		return Allsafelist.keySet();
	}
	
	public HashSet<String> getCurrent() {
		return currentList;
	}
	
	public String getRealName(String apk) {
		return x10List.getProperty(apk);
	}
	
	public String getApkName(String realName) {
		return realnames.getProperty(realName);
	}

	public void setSafe(String apkName) {
		safeList.setProperty(apkName, "safe");
	}
	
	public void setUnsafe(String apkName) {
		safeList.setProperty(apkName, "unsafe");
	}
	
	public Enumeration<String> getToBeRemoved() {
		Vector<String> v = new Vector<String>();
		Iterator<String> ic = currentList.iterator();
		while (ic.hasNext()) {
			String apk=ic.next();
			if (safeList.getProperty(apk).equals("safe")) v.add(apk);
		}
		return v.elements();
	}

	public Enumeration<String> getRemoved() {
		Vector<String> v = new Vector<String>();
		Iterator<Object> il = safeList.keySet().iterator();
		while (il.hasNext()) {
			String apk=(String)il.next();
			if (!currentList.contains(apk) && safeList.getProperty(apk).equals("safe")) v.add(apk);
		}
		return v.elements();		
	}

	public Enumeration<String> getToBeInstalled() {
		Vector<String> v = new Vector<String>();
		Iterator<Object> il = safeList.keySet().iterator();
		while (il.hasNext()) {
			String apk=(String)il.next();
			if (!currentList.contains(apk) && safeList.getProperty(apk).equals("unsafe")) v.add(apk);
		}
		return v.elements();		
	}

	public Enumeration<String> getInstalled() {
		Vector<String> v = new Vector<String>();
		Iterator<String> ic = currentList.iterator();
		while (ic.hasNext()) {
			String apk=ic.next();
			if (safeList.getProperty(apk).equals("unsafe")) v.add(apk);
		}
		return v.elements();
	}
	
}