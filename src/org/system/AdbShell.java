package org.system;

import gui.FlasherGUI;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AdbShell  {
   private ProcessBuilder builder;
   private Process adb;
   private static String fsep = OS.getFileSeparator();

   private InputStream processInput;

   /**
    * Starts the shell 
    */
   public void start() throws IOException  {
	   if (OS.getName().equals("linux"))
		   builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb").getAbsolutePath(), "status-window");
	   else
		   builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb.exe").getAbsolutePath(), "status-window");
      adb = builder.start();
      processInput = adb.getInputStream();
      Scanner sc = new Scanner(processInput);
      while (sc.hasNextLine()) {
    	  String line = sc.nextLine();
    	  if (line.contains("State: device")) FlasherGUI.doIdent();
    	  else if (line.contains("State: unknown")) FlasherGUI.doDisableIdent();
      }
   }

   /**
    * Stop the shell;
    */
   public void stop()   {
      try   {
    	  processInput.close();
    	  adb.destroy();
      }
   catch(Exception ignore)  {}
   }

}
