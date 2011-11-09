package org.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OS {

	public static String getName() {
		  String os = "";
		  if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
		    os = "windows";
		  } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
		    os = "linux";
		  } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
		    os = "mac";
		  }
		 
		  return os;
	}
	
	public static String getAdbPath() {
			String fsep = OS.getFileSeparator();
		   if (OS.getName().equals("linux"))
			   return new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb").getAbsolutePath();
		   else
			   return new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb.exe").getAbsolutePath();	
	}

	public static String getFastBootPath() {
		String fsep = OS.getFileSeparator();
	   if (OS.getName().equals("linux"))
		   return new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"fastboot").getAbsolutePath();
	   else
		   return new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"fastboot.exe").getAbsolutePath();	
}
	
	public static String getMD5(File f) {
		byte[] buffer = new byte[8192];
		int read = 0;
		InputStream is=null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			is = new FileInputStream(f);
			while( (read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}		
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			return output.toUpperCase();
		}
		catch(IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		}
		catch(NoSuchAlgorithmException nsa) {
			throw new RuntimeException("Unable to process file for MD5", nsa);
		}
		finally {
			try {
				is.close();
			}
			catch(IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
	}
	
	public static String getVersion() {
		return System.getProperty("os.version");
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	public static String getWinDir() {
		if (System.getenv("WINDIR")==null) return System.getenv("SYSTEMROOT");
		if (System.getenv("WINDIR").length()==0) return System.getenv("SYSTEMROOT");
		return System.getenv("WINDIR");
	}
	
	public static String getSystem32Dir() {
		return getWinDir()+getFileSeparator()+"System32";
	}
}
