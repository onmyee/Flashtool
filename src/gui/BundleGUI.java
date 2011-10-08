package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import flashsystem.Bundle;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import org.lang.Language;
import org.logger.MyLogger;
import org.system.DeviceEntry;
import org.system.Devices;
import org.system.OS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextField;
import javax.swing.JList;


public class BundleGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JButton okButton;
	private JTextField folderSource;
	private JTextField device;
	private JTextField version;
	private JTextField branding;
	private JList listFolder;
	private JList listFirmware;
	DefaultListModel listFolderModel = new DefaultListModel();
	DefaultListModel listFirmwareModel = new DefaultListModel();
	SortedNameListModel sortedListFolderModel = new SortedNameListModel(listFolderModel);
	SortedNameListModel sortedListFirmwareModel = new SortedNameListModel(listFirmwareModel);
	boolean retcode=false;

	/**
	 * A class that implements the Java FileFilter interface.
	 */
	private void dirlist() {
		listFolderModel.removeAllElements();
		listFirmwareModel.removeAllElements();
		File[] list = (new File(folderSource.getText())).listFiles(new FirmwareFileFilter());
		for (int i=0;i<list.length;i++) {
			listFolderModel.addElement(list[i].getName());
		}
	}
	
	public BundleGUI(String folder) {
		init();
		folderSource.setText(folder);
		dirlist();
	}

	public BundleGUI() {
		init();
	}
	/**
	 * Create the dialog.
	 */
	public void init() {
		setResizable(false);
		setName("BundleGUI");
		setTitle("Bundle Creation");
		setModal(true);
		setBounds(100, 100, 687, 357);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(198dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(36dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(105dlu;default):grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(59dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "2, 2, 3, 1, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("max(197dlu;default)"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblSelectSourceFolder = new JLabel("Select source folder :");
				lblSelectSourceFolder.setName("lblSelectSourceFolder");
				panel.add(lblSelectSourceFolder, "2, 2, 3, 1");
			}
			{
				folderSource  = new JTextField();
				folderSource.setEditable(false);
				panel.add(folderSource, "2, 4, fill, default");
				folderSource.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						doChoose();
					}
				});
				panel.add(button, "4, 4, left, default");
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "6, 2, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblDevice = new JLabel("Device :");
				lblDevice.setName("lblDevice");
				panel.add(lblDevice, "2, 2");
			}
			{
				device = new JTextField();
				panel.add(device, "4, 2");
				device.setColumns(10);
			}
			{
				JLabel lblVersion = new JLabel("Version :");
				lblVersion.setName("lblVersion");
				panel.add(lblVersion, "2, 4");
			}
			{
				version = new JTextField();
				panel.add(version, "4, 4");
				version.setColumns(10);
			}
			{
				JLabel lblBranding = new JLabel("Branding :");
				lblBranding.setName("lblBranding");
				panel.add(lblBranding, "2, 6");
			}
			{
				branding = new JTextField();
				panel.add(branding, "4, 6");
				branding.setColumns(10);
			}
		}
		{
			JLabel lblFirmwareList = new JLabel("Folder list :");
			lblFirmwareList.setName("lblFirmwareList");
			contentPanel.add(lblFirmwareList, "2, 4");
		}
		{
			JLabel lblFirmwareContent = new JLabel("Firmware content :");
			lblFirmwareContent.setName("lblFirmwareContent");
			contentPanel.add(lblFirmwareContent, "6, 4");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 6, 1, 5, fill, fill");
			{
				listFolder = new JList();
				listFolder.setModel(sortedListFolderModel);
				scrollPane.setViewportView(listFolder);
			}
		}
		{
			JButton button = new JButton("->");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listFolder.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listFirmwareModel.addElement(values[i]);
						listFolderModel.removeElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "4, 6, center, default");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "6, 6, 1, 5, fill, fill");
			{
				listFirmware = new JList();
				listFirmware.setModel(sortedListFirmwareModel);
				scrollPane.setViewportView(listFirmware);
			}
		}
		{
			JButton button = new JButton("<-");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listFirmware.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listFirmwareModel.removeElement(values[i]);
						listFolderModel.addElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "4, 8, center, default");
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				retcode=false;
				dispose();
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setName("okButton");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						retcode=(version.getText().length()>0&&device.getText().length()>0&&branding.getText().length()>0 && listFirmwareModel.getSize()>0);
						if (retcode)
							if ((new File("./firmwares/"+device.getText()+"_"+version.getText()+"_"+branding.getText()+".ftf")).exists())
								JOptionPane.showMessageDialog(null, "You already used this device/version/branding for another bundle");
							else {
								if (listFirmwareModel.getSize()==1) {
									String md5 = OS.getMD5(new File(folderSource.getText()+OS.getFileSeparator()+(String)listFirmwareModel.getElementAt(0)));
									Enumeration e = Devices.listDevices(false);
									boolean found = false;
									while (e.hasMoreElements() && !found) {
										DeviceEntry entry = Devices.getDevice((String)e.nextElement());
										if (md5.equals(entry.getLoaderMD5())) found = true;
										if (entry.hasUnlockedLoader())
											if (md5.equals(entry.getLoaderUnlockedMD5())) found = true;
									}
									if (found) {
										JOptionPane.showMessageDialog(null, "A Bundle cannot contain only a loader");
									}
									else {
										dispose();
									}
								}
								else dispose();
							}
						else {
							JOptionPane.showMessageDialog(null, "You must fill all fields and have at least one file in the firmware list");
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setName("cancelButton");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						retcode=false;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		try {
			dirlist();
		}
		catch (Exception e) {}
		setLanguage();
	}

	public void doChoose() {
		JFileChooser chooser = new JFileChooser(); 
		if (folderSource.getText().length()==0)
			chooser.setCurrentDirectory(new java.io.File("."));
		else
			chooser.setCurrentDirectory(new java.io.File(folderSource.getText()));
	    chooser.setDialogTitle("Choose a folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	folderSource.setText(chooser.getSelectedFile().getAbsolutePath());
	    	dirlist();
	    }
	}

	public void setLanguage() {
		Language.translate(this);
	}
	
	public Bundle getBundle() {
		setVisible(true);
		if (retcode) {
			Bundle b = new Bundle(folderSource.getText(),Bundle.FOLDERTYPE);
			Enumeration e = listFolderModel.elements();
			while (e.hasMoreElements()) b.removeEntry((String)e.nextElement());
			deviceSelectGui devsel = new deviceSelectGui(b);
			devsel.isSelected();
			b.setDevice(device.getText());
			b.setVersion(version.getText());
			b.setBranding(branding.getText());
			return b;
		}
		return null;
	}

}