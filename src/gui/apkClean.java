package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import foxtrot.Job;
import foxtrot.Worker;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.adb.AdbUtility;
import org.lang.Language;
import org.logger.MyLogger;
import org.system.OS;
import org.system.Profile;
import org.system.TextFile;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.io.File;

public class apkClean extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static String fsep = OS.getFileSeparator();
	JList listInstalled;
	JList listToRemove;
	JList listRemoved;
	JList listToInstall;
	JComboBox comboProfile;
	DefaultListModel listInstalledModel = new DefaultListModel();
	DefaultListModel listRemovedModel = new DefaultListModel();
	DefaultListModel listToRemoveModel = new DefaultListModel();
	DefaultListModel listToInstallModel = new DefaultListModel();
	SortedNameListModel sortedInstalledModel = new SortedNameListModel(listInstalledModel);
	SortedNameListModel sortedRemovedModel = new SortedNameListModel(listRemovedModel);
	SortedNameListModel sortedToRemoveModel = new SortedNameListModel(listToRemoveModel);
	SortedNameListModel sortedToInstallModel = new SortedNameListModel(listToInstallModel);
	TextFile appsremove = new TextFile("."+fsep+"custom"+fsep+"clean"+fsep+"listappsremove","ASCII");
	TextFile appsadd = new TextFile("."+fsep+"custom"+fsep+"clean"+fsep+"listappsadd","ASCII");
	X10Apps apps;
	ItemListener profileChange;
	JMenuItem mntmSave;
	JMenuItem mntmExport;


    public void dumpToRemoveToFile() throws IOException {
    	if (sortedToRemoveModel.getSize()>0) {
	    	appsremove.open(false);
			boolean first = true;
			for (int i = 0; i<sortedToRemoveModel.getSize();i++) {
				ListItem li = (ListItem)sortedToRemoveModel.getElementAt(i);
				if (first) {
					appsremove.write(li.getFile());
					first = false;
				}
				else {
					appsremove.writeln("");
					appsremove.write(li.getFile());
				}
			}
			appsremove.close();
    	}
    }

    public void dumpToAddToFile() throws IOException {
    	if (sortedToInstallModel.getSize()>0) {
	    	appsadd.open(false);
			boolean first = true;
			for (int i = 0; i<sortedToInstallModel.getSize();i++) {
				ListItem li = (ListItem)sortedToInstallModel.getElementAt(i);
				if (first) {
					appsadd.write(li.getFile());
					first = false;
				}
				else {
					appsadd.writeln("");
					appsadd.write(li.getFile());
				}
			}
			appsadd.close();
    	}
    }

	/**
	 * Create the dialog.
	 */
	public apkClean() {
		setName("apkClean");
		setTitle("SE ROM Cleaner");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 580, 532);
		{
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				JMenu mnEdit = new JMenu("Edit");
				mnEdit.setName(getName()+"_mnEdit");
				menuBar.add(mnEdit);
				{
					{
						JMenuItem mntmApplicationListManager = new JMenuItem("Application List Manager");
						mntmApplicationListManager.setName("mntmApplicationListManager");
						mntmApplicationListManager.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								AppListManager manager = new AppListManager(apps);
								manager.setVisible(true);
								fillLists();
							}
						});
						mnEdit.add(mntmApplicationListManager);
					}
					{
						JMenu mnProfiles = new JMenu("Profiles");
						mnEdit.add(mnProfiles);
						mntmSave = new JMenuItem("Save");
						mnProfiles.add(mntmSave);
						mntmSave.setName(getName()+"_mntmSave");
						mntmSave.setEnabled(false);
						{
							JMenuItem mntmSaveAs = new JMenuItem("Save As");
							mnProfiles.add(mntmSaveAs);
							mntmSaveAs.setName(getName()+"_mntmSaveAs");
							{
								mntmExport = new JMenuItem("Export");
								mntmExport.setEnabled(false);
								mntmExport.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										Profile p = new Profile(apps);
									}
								});
								mnProfiles.add(mntmExport);
							}
							{
								JMenuItem mntmImport = new JMenuItem("Import");
								mntmImport.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										ProfileImport imp = new ProfileImport("./custom/clean");
										String profile=imp.getProfile();
										if (profile.length()>0) {
											try {
												JarFile j = new JarFile(profile);
												Enumeration<JarEntry> e = j.entries();
												while (e.hasMoreElements()) {
													JarEntry entry = e.nextElement();
													if (entry.getName().toLowerCase().endsWith("apk")) {
														InputStream in = j.getInputStream(entry);
														String outname = "."+OS.getFileSeparator()+"custom"+OS.getFileSeparator()+"apps_saved"+OS.getFileSeparator()+entry.getName();
														MyLogger.getLogger().debug("Writing Entry to "+outname);
														OutputStream out = new BufferedOutputStream(new FileOutputStream(outname));
														byte[] buffer = new byte[1024];
														int len;
														while((len = in.read(buffer)) >= 0)
															out.write(buffer, 0, len);
														in.close();
														out.close();
													}
												}
												e = j.entries();
												while (e.hasMoreElements()) {
													JarEntry entry = e.nextElement();
													if (entry.getName().toLowerCase().startsWith("safelist")) {
														InputStream in = j.getInputStream(entry);
														String outname = "."+OS.getFileSeparator()+"custom"+OS.getFileSeparator()+"clean"+OS.getFileSeparator()+entry.getName();
														MyLogger.getLogger().debug("Writing Entry to "+outname);
														OutputStream out = new BufferedOutputStream(new FileOutputStream(outname));
														byte[] buffer = new byte[1024];
														int len;
														while((len = in.read(buffer)) >= 0)
															out.write(buffer, 0, len);
														in.close();
														out.close();														
													}
													if (entry.getName().toLowerCase().startsWith("custom")) {
														InputStream in = j.getInputStream(entry);
														Properties p = new Properties();
														p.load(in);
														Iterator i = p.keySet().iterator();
														while (i.hasNext()) {
															String apk = (String)i.next();
															String desc = p.getProperty(apk);
															apps.addApk(new File("./custom/apps_saved/"+apk), desc);
														}
														in.close();
													}
												}
												comboProfile.addItem(j.getManifest().getMainAttributes().getValue("profileName"));
												fillLists();
											} catch (IOException e) {}
										}
									}
								});
								mnProfiles.add(mntmImport);
							}
							mntmSaveAs.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									ProfileSaveAs ps = new ProfileSaveAs();
									if (ps.SaveAs(apps)) {
									comboProfile.removeItemListener(profileChange);
									comboProfile.removeAllItems();
									Iterator<String> itprofiles = apps.getProfiles().iterator();
									while (itprofiles.hasNext()) {
										comboProfile.addItem(itprofiles.next());
									}
									comboProfile.setSelectedItem(apps.getCurrentProfile());
									mntmSave.setEnabled(true);
									mntmExport.setEnabled(true);
									comboProfile.addItemListener(profileChange);
									}
								}
							});
						}
						mntmSave.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								apps.saveProfile();
							}
						});
					}
				}
			}
		}
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(134dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("51px"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(16dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(53dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(13dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(13dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(13dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(13dlu;default):grow"),}));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "2, 2, 7, 1, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblProfile = new JLabel("Profile :");
				panel.add(lblProfile, "2, 2");
			}
			{
				comboProfile = new JComboBox();
				profileChange = new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						if (arg0.getStateChange()==ItemEvent.SELECTED) {
						if (!apps.getCurrentProfile().equals((String)comboProfile.getSelectedItem())) {
							if (((String)comboProfile.getSelectedItem()).equals("default")) {
								mntmSave.setEnabled(false);
								mntmExport.setEnabled(false);
							}
							else {
								mntmSave.setEnabled(true);
								mntmExport.setEnabled(true);
							}
							apps.setProfile((String)comboProfile.getSelectedItem());
							fillLists();
						}
						}
					}
				};
				
				panel.add(comboProfile, "4, 2");
			}
		}
		{
			JLabel lblInstalled = new JLabel("Installed on device :");
			lblInstalled.setName("lblInstalled");
			contentPanel.add(lblInstalled, "2, 4");
		}
		{
			JLabel lblToBeRemoved = new JLabel("To be removed :");
			lblToBeRemoved.setName("lblToBeRemoved");
			contentPanel.add(lblToBeRemoved, "6, 4");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 6, 1, 7, fill, fill");
			{
				listInstalled = new JList();
				listInstalled.setCellRenderer(new MyCellRenderer());
				listInstalled.setModel(sortedInstalledModel);
				scrollPane.setViewportView(listInstalled);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "6, 6, 3, 7, fill, fill");
			{
				listToRemove = new JList();
				listToRemove.setModel(sortedToRemoveModel);
				listToRemove.setCellRenderer(new MyCellRenderer());
				scrollPane.setViewportView(listToRemove);
			}
		}
		{
			JButton button = new JButton("->");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] values = listInstalled.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listToRemoveModel.addElement(values[i]);
						apps.setSafe(((ListItem)values[i]).getFile());
						listInstalledModel.removeElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "4, 8");
		}
		{
			JButton button = new JButton("<-");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] values = listToRemove.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listToRemoveModel.removeElement(values[i]);
						apps.setUnsafe(((ListItem)values[i]).getFile());
						listInstalledModel.addElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "4, 10");
		}
		{
			JLabel lblRemovedFromDevice = new JLabel("Available for installation :");
			lblRemovedFromDevice.setName("lblRemovedFromDevice");
			contentPanel.add(lblRemovedFromDevice, "2, 14");
		}
		{
			JLabel lblToBeInstalled = new JLabel("To be installed :");
			lblToBeInstalled.setName("lblToBeInstalled");
			contentPanel.add(lblToBeInstalled, "6, 14");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 16, 1, 7, fill, fill");
			{
				listRemoved = new JList();
				listRemoved.setModel(sortedRemovedModel);
				listRemoved.setCellRenderer(new MyCellRenderer());
				scrollPane.setViewportView(listRemoved);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "6, 16, 3, 7, fill, fill");
			{
				listToInstall = new JList();
				listToInstall.setModel(sortedToInstallModel);
				listToInstall.setCellRenderer(new MyCellRenderer());
				scrollPane.setViewportView(listToInstall);
			}
		}
		{
			JButton button = new JButton("->");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listRemoved.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listToInstallModel.addElement(values[i]);
						apps.setUnsafe(((ListItem)values[i]).getFile());
						listRemovedModel.removeElement(values[i]);
					}				
				}
			});
			contentPanel.add(button, "4, 18");
		}
		{
			JButton button = new JButton("<-");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listToInstall.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listRemovedModel.addElement(values[i]);
						apps.setSafe(((ListItem)values[i]).getFile());
						listToInstallModel.removeElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "4, 20");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							dumpToRemoveToFile();
							dumpToAddToFile();
						}
						catch (IOException ioe) {
							appsremove.delete();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				okButton.setName("okButton");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setName("cancelButton");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						appsremove.delete();
						appsadd.delete();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		try {
			apps = new X10Apps();
			Iterator<String> itprofiles = apps.getProfiles().iterator();
			while (itprofiles.hasNext()) {
				comboProfile.addItem(itprofiles.next());
			}
			apps.setProfile("default");
			comboProfile.setSelectedItem("default");
			comboProfile.addItemListener(profileChange);
			fillLists();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		listInstalled.setSelectedIndex(0);
		listRemoved.setSelectedIndex(0);
		{
			JPopupMenu popupMenu = new JPopupMenu();
			addPopup(listRemoved, popupMenu);
			{
				JMenuItem mntmAdd = new JMenuItem("Add");
				mntmAdd.setName("mntmAdd");
				mntmAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						CustomAppList list = new CustomAppList(apps);
						String result = list.getApp();
						if (result.length()>0) {
							apps.setSafe(result);
							fillLists();
						}
					}
				});
				popupMenu.add(mntmAdd);
			}
		}
		setLanguage();
	}

	public void fillLists() {
		listInstalledModel.removeAllElements();
		listToRemoveModel.removeAllElements();
		listToInstallModel.removeAllElements();
		listRemovedModel.removeAllElements();
		Enumeration<String> e1 = apps.getInstalled();
		while (e1.hasMoreElements()) {
			String elem = e1.nextElement();
			ListItem li = new ListItem(elem, apps.getRealName(elem),Color.black,Color.white);
			listInstalledModel.addElement(li);
		}
		Enumeration<String> e2 = apps.getToBeRemoved();
		while (e2.hasMoreElements()) {
			String elem = e2.nextElement();
			ListItem li = new ListItem(elem, apps.getRealName(elem),Color.black,Color.white);
			listToRemoveModel.addElement(li);
		}
		Enumeration<String> e3 = apps.getToBeInstalled();
		while (e3.hasMoreElements()) {
			String elem = e3.nextElement();
			ListItem li = new ListItem(elem, apps.getRealName(elem),Color.black,Color.white);
			listToInstallModel.addElement(li);
		}
		Enumeration<String> e4 = apps.getRemoved();
		while (e4.hasMoreElements()) {
			String elem = e4.nextElement();
			ListItem li = new ListItem(elem, apps.getRealName(elem),Color.black,Color.white);
			listRemovedModel.addElement(li);
		}
	}
	
	public void setLanguage() {
		Language.translate(this);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
