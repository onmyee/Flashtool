package gui;

import foxtrot.Job;
import foxtrot.Worker;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.adb.AdbUtility;
import org.adb.FastbootUtility;
import org.lang.Language;
import org.logger.MyLogger;
import org.system.Device;
import org.system.RunOutputs;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JTextPane;

public class FastBootToolboxGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public static final String version = "1.0";
	public static final String msg1 = "Fastboot Toolbox";
	public static final String msg2 = "by DooMLoRD";


	/**
	 * Create the dialog.
	 */
	public FastBootToolboxGUI(){
		
		MyLogger.info("Launching " + msg1 + " " + version + " " + msg2);
		
		
		setTitle("Fastboot Toolbox");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 530, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblVersion = new JLabel("Version "+version);
			lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
			lblVersion.setBounds(10, 11, 107, 14);
			contentPanel.add(lblVersion);
		}
		{
			JLabel lblFrom = new JLabel(msg2);
			lblFrom.setHorizontalAlignment(SwingConstants.CENTER);
			lblFrom.setBounds(397, 11, 102, 14);
			contentPanel.add(lblFrom);
		}
		
		{
			JButton btnRebootIntoFastbootADB = new JButton("Reboot into fastboot mode (via ADB)");
			btnRebootIntoFastbootADB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					adbRebootIntoFastboot();
					
				}

			});
			btnRebootIntoFastbootADB.setBounds(31, 76, 211, 23);
			contentPanel.add(btnRebootIntoFastbootADB);
		}
		
		{
			JButton btnFastbootReboot = new JButton("Reboot device into system");
			btnFastbootReboot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					fastbootReboot();

				}
				});
			btnFastbootReboot.setBounds(159, 218, 184, 23);
			contentPanel.add(btnFastbootReboot);
		}
		
		{
			JButton btnRebootIntoFastbootFB = new JButton("Reboot into fastboot mode (via Fastboot)");
			btnRebootIntoFastbootFB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					rebootBackIntoFastbootMode();

				}
			});
			btnRebootIntoFastbootFB.setBounds(266, 76, 235, 23);
			contentPanel.add(btnRebootIntoFastbootFB);
		}
		
		{
			JButton btnGetDeviceInfo = new JButton("Get Device Info");
			btnGetDeviceInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					getConnectedDeviceInfo();

				}
			});
			btnGetDeviceInfo.setBounds(266, 173, 233, 23);
			contentPanel.add(btnGetDeviceInfo);
		}
		
		{
			JButton btnGetVerInfo = new JButton("Get Ver Info");
			btnGetVerInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
					getFastbootVerInfo();

				}
			});
			btnGetVerInfo.setBounds(31, 173, 211, 23);
			contentPanel.add(btnGetVerInfo);
		}
		{
			JButton btnSelectKernelToHotBoot = new JButton("Select kernel to HotBoot");
			btnSelectKernelToHotBoot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					hotbootKernel();
					
				}
			});
			btnSelectKernelToHotBoot.setBounds(31, 122, 211, 23);
			contentPanel.add(btnSelectKernelToHotBoot);
		}
		{
			JButton btnSelectKernelToFlash = new JButton("Select kernel to Flash");
			btnSelectKernelToFlash.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					flashKernel();
					
				}
			});
			btnSelectKernelToFlash.setBounds(266, 122, 233, 23);
			contentPanel.add(btnSelectKernelToFlash);
		}
		
	
		// CURRENTLY DISABLED
		/*
		JButton btnFactoryReset = new JButton("WIPE USER DATA");
		btnFactoryReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				doFactoryReset();
				
			}
		});
		btnFactoryReset.setBounds(371, 173, 128, 23);
		contentPanel.add(btnFactoryReset);
		
		*/
		
		JButton btnCheck = new JButton("CHECK Current Device Status");
		btnCheck.setBounds(159, 26, 184, 23);
		contentPanel.add(btnCheck);
		btnCheck.setHorizontalAlignment(SwingConstants.LEFT);		
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				checkDeviceStatus();

			}
		});
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						MyLogger.info("Finished " + msg1);
						
						dispose();
						
					}
				});
				{
					JLabel lblMoveThisWindow = new JLabel("Move this window to the side so that you are able to see contents of main flashtool log.");
					buttonPane.add(lblMoveThisWindow);
				}
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
				getRootPane().setDefaultButton(closeButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//setLanguage();
		
		checkDeviceStatus();
		
	}
	
	public void doFactoryReset(){
		
		/* add confirmation prompt here */
		
		// FOR AskBox
		
		/*
		Worker.post(new Job() {
			public Object run() {
				
				MyLogger.info("Wiping USERDATA");

				try {
					FastbootUtility.wipeDataCache();
					MyLogger.info("wiping USERDATA complete");
					FastbootUtility.rebootDevice();
					MyLogger.info("Device will now exit fastboot mode and start booting into system");
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
		*/		

	}
	
	public void checkDeviceStatus(){

		String deviceStatus="NOT FOUND"; 

		if (AdbUtility.isConnected()){
			deviceStatus="ADB mode";
		}
		else
		{
			String device = Device.getDeviceIdFastbootMode();
			if (!device.startsWith("Err")) {
				deviceStatus="FASTBOOT mode";
			}

		}
		MyLogger.info("Device Status: " + deviceStatus);
	}
	
	
	public void adbRebootIntoFastboot(){
		Worker.post(new Job() {
			public Object run() {
				MyLogger.info("Please wait device is rebooting into fastboot mode (via ADB)");

				try {
					FastbootUtility.adbRebootFastboot();
					MyLogger.info("Device will soon enter fastboot mode");
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});		
	}
	
	public void fastbootReboot(){
		
		Worker.post(new Job() {
			public Object run() {
				MyLogger.info("Device will now exit fastboot mode and start booting into system");

				try {
					FastbootUtility.rebootDevice();
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
	}
	
	public void rebootBackIntoFastbootMode(){
		
		Worker.post(new Job() {
			public Object run() {
				MyLogger.info("Please wait device is rebooting into fastboot mode (via Fastboot)");

				try {
					FastbootUtility.rebootFastboot();
					MyLogger.info("Device will soon reboot back into fastboot mode");
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
	}
	
	public void getConnectedDeviceInfo(){
		
		Worker.post(new Job() {
			public Object run() {

				MyLogger.info("Fetching connected device info");

				try {
					RunOutputs outputsRun = FastbootUtility.getDeviceInfo();
					MyLogger.info("Connected device info: [ " + outputsRun.getStdOut().split("fastboot")[0].trim() + " ]");
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
	}
	
	public void getFastbootVerInfo(){
		Worker.post(new Job() {
			public Object run() {

				MyLogger.info("Fetching fastboot version info from connected device");

				try {
					RunOutputs outputsRun = FastbootUtility.getFastbootVerInfo();
					MyLogger.info("FASTBOOT version info: [ " + outputsRun.getStdErr().split("\n")[0].trim() + " ]");
					
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
	}
	
	
	public void setLanguage() {
		Language.translate(this);
	}

	public void hotbootKernel() {
		Worker.post(new Job() {
			public Object run() {
				try {
					String kernel = chooseKernel();
					
					if(kernel.equals("ERROR")) {
						MyLogger.error("no kernel (boot.img or kernel.sin) selected!");
					} 
					else {
						
						MyLogger.info("Selected kernel (boot.img or kernel.sin): " + kernel);

						// just to make sure that device is in fastboot mode
						MyLogger.debug("rebooting device into fastboot mode");
						FastbootUtility.adbRebootFastboot();
						// this wont wait for reply and will move on to next command

						MyLogger.info("HotBooting selected kernel");
						RunOutputs outputsRun = FastbootUtility.hotBoot(kernel);
						MyLogger.info("FASTBOOT Output: \n " + outputsRun.getStdErr().trim() + "\n");
						MyLogger.info("Device should now start booting with this kernel");
					}
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});

	}
	
	public void flashKernel() {
		Worker.post(new Job() {
			public Object run() {
				try {
					String kernel = chooseKernel();

					if(kernel.equals("ERROR")) {
						MyLogger.error("no kernel (boot.img or kernel.sin) selected!");
					} 
					else {

						MyLogger.info("Selected kernel (boot.img or kernel.sin): " + kernel);

						// just to make sure that device is in fastboot mode
						MyLogger.debug("rebooting device into fastboot mode");
						FastbootUtility.adbRebootFastboot();
						// this wont wait for reply and will move on to next command

						MyLogger.info("Flashing selected kernel");
						RunOutputs outputsRun = FastbootUtility.flashBoot(kernel);
						MyLogger.info("FASTBOOT Output: \n " + outputsRun.getStdErr().trim() + "\n");

						MyLogger.info("Rebooting device");
						FastbootUtility.rebootDevice();
					}
				}
				catch (Exception e1) {
					MyLogger.error(e1.getMessage());
				}
				return null;
			}
		});
	}
	
	public String chooseKernel() {
		JFileChooser chooser = new JFileChooser(new java.io.File(".")); 

		FileFilter ff = new FileFilter(){
			public boolean accept(File f){
				if(f.isDirectory()) return true;
				else if(f.getName().endsWith(".img")) return true;
				else if(f.getName().equals("kernel.sin")) return true;
				else return false;
			}
			public String getDescription(){
				return "kernel IMG file or kernel.sin";
			}
		};
		 
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(ff);
		
	    chooser.setDialogTitle("Choose kernel file (boot.img or kernel.sin)");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    //chooser.setFileFilter(newkernelimgFileFilter);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getAbsolutePath();
	    }
	    return "ERROR";
	}
}
