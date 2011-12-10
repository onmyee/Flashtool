package flashsystem;

import java.io.File;

import gui.About;
import gui.FlasherGUI;

import org.logger.MyLogger;
import org.system.DeviceChangedListener;

public class FlasherConsole {

	public static void main(String[] args) throws Exception {
		MyLogger.disableTextArea();
		MyLogger.setLevel("info");
		MyLogger.getLogger().info("Flashtool "+About.getVersion());
		FlasherGUI.guimode=false;
		DeviceChangedListener.start();
		Bundle b = new Bundle(new File(args[0]).getAbsolutePath(),Bundle.JARTYPE);
		b.setSimulate(false);
		b.setWipeData(args[1].equals("yes"));
		b.setWipeCache(args[2].equals("yes"));
		b.setExcludeBB(args[3].equals("no"));
		b.setExcludeSystem(args[4].equals("no"));
		b.setExcludeKernel(args[5].equals("no"));
		b.open();
		X10flash f = new X10flash(b);
		MyLogger.getLogger().info("Please connect your phone in flash mode");
		while (!f.deviceFound());
		f.openDevice(false);
		f.flashDevice();
		b.close();
		MyLogger.getLogger().info("Now unplug the device and power it on");
		MyLogger.getLogger().info("Then go to application settings");
		MyLogger.getLogger().info("turn on Unknown Sources and Debugging");
		System.exit(0);
	}
}