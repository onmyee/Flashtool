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

import org.adb.FastbootUtility;
import org.lang.Language;
import org.logger.MyLogger;

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
			JLabel lblName = new JLabel("Fastboot Toolbox");
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			lblName.setBounds(10, 11, 504, 14);
			contentPanel.add(lblName);
		}
		{
			JLabel lblVersion = new JLabel("Version "+version);
			lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
			lblVersion.setBounds(10, 36, 504, 14);
			contentPanel.add(lblVersion);
		}
		{
			JLabel lblFrom = new JLabel(msg2);
			lblFrom.setHorizontalAlignment(SwingConstants.CENTER);
			lblFrom.setBounds(10, 61, 504, 14);
			contentPanel.add(lblFrom);
		}
		
		{
			JButton btnRebootIntoFastbootADB = new JButton("Reboot into fastboot mode (via ADB)");
			btnRebootIntoFastbootADB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					adbRebootIntoFastboot();
					
				}

			});
			btnRebootIntoFastbootADB.setBounds(29, 106, 211, 23);
			contentPanel.add(btnRebootIntoFastbootADB);
		}
		
		{
			JButton btnFastbootReboot = new JButton("Reboot device into system");
			btnFastbootReboot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					fastbootReboot();

				}
				});
			btnFastbootReboot.setBounds(336, 205, 163, 23);
			contentPanel.add(btnFastbootReboot);
		}
		
		{
			JButton btnRebootIntoFastbootFB = new JButton("Reboot into fastboot mode (via Fastboot)");
			btnRebootIntoFastbootFB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					rebootBackIntoFastbootMode();

				}
			});
			btnRebootIntoFastbootFB.setBounds(264, 106, 235, 23);
			contentPanel.add(btnRebootIntoFastbootFB);
		}
		
		{
			JButton btnGetDeviceInfo = new JButton("Get Device Info");
			btnGetDeviceInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					getConnectedDeviceInfo();

				}
			});
			btnGetDeviceInfo.setBounds(182, 205, 128, 23);
			contentPanel.add(btnGetDeviceInfo);
		}
		
		{
			JButton btnGetVerInfo = new JButton("Get Ver Info");
			btnGetVerInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
					getFastbootVerInfo();

				}
			});
			btnGetVerInfo.setBounds(31, 205, 128, 23);
			contentPanel.add(btnGetVerInfo);
		}
		{
			JButton btnSelectBootimgToHotBoot = new JButton("Select boot.img to HotBoot");
			btnSelectBootimgToHotBoot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					hotbootKernel();
					
				}
			});
			btnSelectBootimgToHotBoot.setBounds(75, 159, 163, 23);
			contentPanel.add(btnSelectBootimgToHotBoot);
		}
		{
			JButton btnSelectBootimgToFlash = new JButton("Select boot.img to Flash");
			btnSelectBootimgToFlash.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					flashKernel();
					
				}
			});
			btnSelectBootimgToFlash.setBounds(265, 159, 163, 23);
			contentPanel.add(btnSelectBootimgToFlash);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					
						dispose();
						
					}
				});
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
				getRootPane().setDefaultButton(closeButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//setLanguage();
	}
	
	public void adbRebootIntoFastboot(){
		Worker.post(new Job() {
			public Object run() {
				MyLogger.info("Reboot into fastboot mode (via ADB)");

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
				MyLogger.info("Rebooting device into system, device will now exit fastboot mode and start booting");

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
				MyLogger.info("Reboot into fastboot mode (via Fastboot)");

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
					FastbootUtility.getDeviceInfo();
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
					FastbootUtility.getFastbootVerInfo();
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
					String bootimg = chooseBootimg();
					
					if(bootimg.equals("ERROR")) {
						MyLogger.error("no kernel (boot.img) selected!");
					} 
					else {
						
						MyLogger.info("Selected kernel (boot.img): " + bootimg);

						// just to make sure that device is in fastboot mode
						MyLogger.debug("rebooting device into fastboot mode");
						FastbootUtility.adbRebootFastboot();
						// this wont wait for reply and will move on to next command

						MyLogger.info("HotBooting selected kernel");
						FastbootUtility.hotBoot(bootimg);
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
					String bootimg = chooseBootimg();

					if(bootimg.equals("ERROR")) {
						MyLogger.error("no kernel (boot.img) selected!");
					} 
					else {

						MyLogger.info("Selected kernel (boot.img): " + bootimg);

						// just to make sure that device is in fastboot mode
						MyLogger.debug("rebooting device into fastboot mode");
						FastbootUtility.adbRebootFastboot();
						// this wont wait for reply and will move on to next command

						MyLogger.info("Flashing selected kernel");
						FastbootUtility.flashBoot(bootimg);

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
	
	public String chooseBootimg() {
		JFileChooser chooser = new JFileChooser(new java.io.File(".")); 

		FileFilter ff = new FileFilter(){
			public boolean accept(File f){
				if(f.isDirectory()) return true;
				else if(f.getName().endsWith(".img")) return true;
					else return false;
			}
			public String getDescription(){
				return "IMG files";
			}
		};
		 
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(ff);
		
	    chooser.setDialogTitle("Choose kernel file (boot.img)");
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
