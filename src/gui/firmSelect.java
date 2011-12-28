package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import flashsystem.Bundle;
import flashsystem.BundleEntry;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarFile;

import javax.swing.ListSelectionModel;

import org.lang.Language;
import org.logger.MyLogger;
import org.system.GlobalConfig;
import org.system.OS;
import org.system.PropertiesFile;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;


public class firmSelect extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String fsep = OS.getFileSeparator();
	private final JPanel contentPanel = new JPanel();
	private JTable tableFirm;
	private DefaultTableModel modelFirm;
	private DefaultTableModel model_1;
	private XTableColumnModel tableColumnModel = new XTableColumnModel(); 
	private Bundle selected=null;
	private String result;
	private boolean retcode = false;
	JButton okButton;
	JCheckBox chckbxWipeUserdata;
	JCheckBox chckbxWipeCache;
	JCheckBox chckbxExcludeSystem;
	JCheckBox chckbxExcludeBB;
	JCheckBox chckbxExcludeKernel;
	private JTable table_1;
	private JTextField folderSource;
	private String filename="";

	private void dirlist() throws Exception{
		boolean hasElements = false;
		modelFirm = new DefaultTableModel();
		modelFirm.addColumn("File");
		modelFirm.addColumn("Device");
		modelFirm.addColumn("Version");
		modelFirm.addColumn("Branding");
		tableFirm.setModel(modelFirm);
		tableFirm.setColumnModel(tableColumnModel);
		tableFirm.createDefaultColumnsFromModel();
		tableColumnModel.setColumnVisible(tableColumnModel.getColumnByModelIndex(0), false);
		if (GlobalConfig.getProperty("bundle").equals("file")) {
	    	File dir = new File(folderSource.getText());
		    File[] chld = dir.listFiles();
		    for(int i = 0; i < chld.length; i++){
		    	if (filename.length()==0) {
		    		if (chld[i].isFile() && chld[i].getName().toUpperCase().endsWith("FTF")) {
		    			hasElements = true;
		    			JarFile jf = new JarFile(chld[i]);
		    			modelFirm.addRow(new String[]{chld[i].getName(),jf.getManifest().getMainAttributes().getValue("device"),jf.getManifest().getMainAttributes().getValue("version"),jf.getManifest().getMainAttributes().getValue("branding")});
		    			MyLogger.getLogger().debug("Adding "+chld[i].getName()+" to list of firmwares");
		    		}
		    	}
		    	else {
		    		if (chld[i].getName().equals(filename)) {
		    			hasElements = true;
		    			JarFile jf = new JarFile(chld[i]);
		    			modelFirm.addRow(new String[]{chld[i].getName(),jf.getManifest().getMainAttributes().getValue("device"),jf.getManifest().getMainAttributes().getValue("version"),jf.getManifest().getMainAttributes().getValue("branding")});
		    			MyLogger.getLogger().debug("Adding "+chld[i].getName()+" to list of firmwares");
		    		}
		    	}
		    }
	    }
	    else {
	    	File dir = new File(folderSource.getText());
		    File[] chld = dir.listFiles();
		    for(int i = 0; i < chld.length; i++){
		    	if (chld[i].isDirectory()) {
		    			PropertiesFile content = new PropertiesFile("",chld[i].getPath()+fsep+"/content.properties");		    			
		    			if (content.getProperty("device")!=null && content.getProperty("version")!=null || content.getProperty("branding")!=null) {
		    				hasElements = true;
		    				modelFirm.addRow(new String[]{chld[i].getPath(),content.getProperty("device"),content.getProperty("version"),content.getProperty("branding")});
		    			}
		    	}
		    }	    	
	    }
	    if (!hasElements) {
	    	okButton.setEnabled(false);
	    	result=null;
	    }
	    else {
	    	tableFirm.setRowSelectionInterval(0, 0);
	    	result=(String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 0);
	    	okButton.setEnabled(true);
	    }
	}

	public void filelist() throws Exception {
		model_1 = new DefaultTableModel();
		model_1.addColumn("File");
		table_1.setModel(model_1);
		boolean hasElements = false;
		boolean hasUserData = false;
		boolean hasSystem = false;
		boolean hasBB = false;
		boolean hasCache = false;
		boolean hasKernel = false;
		if (result!=null) {
			if (GlobalConfig.getProperty("bundle").equals("file")) {
				selected=new Bundle(folderSource.getText()+fsep+result,Bundle.JARTYPE);
				selected.setDevice((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 1));
				selected.setVersion((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 2));
				selected.setBranding((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 3));
				Enumeration<BundleEntry> e = selected.allEntries();
				while (e.hasMoreElements()) {
			    	hasElements = true;
			    	BundleEntry el = e.nextElement();
			    	if (el.getName().toUpperCase().startsWith("USERDATA")) {
			    		if (chckbxWipeUserdata.isSelected()) model_1.addRow(new String[]{el.getName()});
			    		hasUserData=true;
			    	}
				    if (el.getName().toUpperCase().startsWith("CACHE")) {
				    	if (chckbxWipeCache.isSelected()) model_1.addRow(new String[]{el.getName()});
				    	hasCache=true;
				    }
			    	if (el.getName().toUpperCase().startsWith("SYSTEM")) {
				    	if (!chckbxExcludeSystem.isSelected()) model_1.addRow(new String[]{el.getName()});
				    	hasSystem=true;
				    }
			    	if (el.getName().toUpperCase().startsWith("KERNEL")) {
				    	if (!chckbxExcludeKernel.isSelected()) model_1.addRow(new String[]{el.getName()});
				    	hasKernel=true;
			    	}
				    if (!el.getName().toUpperCase().startsWith("KERNEL") &&
				    	!el.getName().toUpperCase().startsWith("LOADER") &&
				    	!el.getName().toUpperCase().startsWith("USERDATA") &&
				    	!el.getName().toUpperCase().startsWith("CACHE") &&
				    	!el.getName().toUpperCase().startsWith("SYSTEM")) {
				    	if (!chckbxExcludeBB.isSelected()) model_1.addRow(new String[]{el.getName()});
				    	hasBB=true;
				    }
				    if (el.getName().toUpperCase().startsWith("LOADER"))
				    	model_1.addRow(new String[]{el.getName()});
			    	MyLogger.getLogger().debug("Adding "+el.getName()+" to the content of "+result);
			    }
			}
			else {
				String[] list = (new File(result)).list();
				for (int i=0;i<list.length;i++) {
					if (list[i].toUpperCase().endsWith("SIN")||list[i].toUpperCase().endsWith("TA")) {
						hasElements=true;
			    		if (list[i].toUpperCase().startsWith("USERDATA")) {
			    			if (chckbxWipeUserdata.isSelected()) model_1.addRow(new String[]{list[i]});
			    			hasUserData=true;
			    		}
				    	else
					    	if (list[i].toUpperCase().startsWith("SYSTEM")) {
					    		if (!chckbxExcludeSystem.isSelected()) model_1.addRow(new String[]{list[i]});
					    		hasSystem=true;
					    	}
					    	else
					    		model_1.addRow(new String[]{list[i]});
					}
				}
			}
		    if (hasElements)
		    	table_1.setRowSelectionInterval(0, 0);
		    chckbxWipeUserdata.setVisible(hasUserData);
		    chckbxWipeCache.setVisible(hasCache);
		    chckbxExcludeSystem.setVisible(hasSystem);
		    chckbxExcludeKernel.setVisible(hasKernel);
		    chckbxExcludeBB.setVisible(hasBB);
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public firmSelect(String path, String file) {
		filename=file;
		setResizable(false);
		setName("firmSelect");
		setTitle("Firmware Selection");
		setModal(true);
		setBounds(100, 100, 772, 353);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(148dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(78dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(81dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("29dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				retcode=false;
				dispose();
			}
		});
		{
			{
				model_1 = new DefaultTableModel();
				model_1.addColumn("File");
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "2, 2, 5, 1, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblSelectSourceFolder = new JLabel("Select Source Folder :");
				lblSelectSourceFolder.setName("lblSelectSourceFolder");
				panel.add(lblSelectSourceFolder, "1, 1, 2, 1");
			}
			{
				folderSource = new JTextField();
				folderSource.setEditable(false);
				if (path.length()==0)
					folderSource.setText(OS.getWorkDir()+fsep+"firmwares");
				else
					folderSource.setText(path);
				panel.add(folderSource, "1, 3, 2, 1, fill, default");
				folderSource.setColumns(10);
			}
			{
				JButton btnNewButton = new JButton("...");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						doChoose();
					}
				});
				panel.add(btnNewButton, "4, 3");
			}
		}
		{
			JLabel lblSelectFirmware = new JLabel("Select Firmware");
			lblSelectFirmware.setName(getName()+"_"+"lblSelectFirmware");
			contentPanel.add(lblSelectFirmware, "2, 4, left, fill");
		}
		{
			JLabel lblFilesInThis = new JLabel("Firmware Content :");
			lblFilesInThis.setName(getName()+"_"+"lblFilesInThis");
			contentPanel.add(lblFilesInThis, "4, 4, left, fill");
		}
		{
			chckbxWipeUserdata = new JCheckBox("Wipe userdata");
			chckbxWipeUserdata.setName(getName()+"_chckbxWipeUserdata");
			chckbxWipeUserdata.setSelected(true);
			chckbxWipeUserdata.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						filelist();
					}
					catch (Exception e) {}
				}
			});
			contentPanel.add(chckbxWipeUserdata, "6, 4, left, fill");
		}
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "2, 6, 1, 9, left, fill");
		tableFirm = new JTable() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int vColIndex) {
		        return false;
		    }
		};
		tableFirm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				result=(String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 0);
				try {
					filelist();
				}
				catch (Exception e) {}
			}
		});
		tableFirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				result=(String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 0);
				try {
					filelist();
				}
				catch (Exception e) {}
			}
		});
		tableFirm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tableFirm);
		{
			JScrollPane scrollPane_1 = new JScrollPane();
			contentPanel.add(scrollPane_1, "4, 6, 1, 9, fill, fill");
			{
				table_1 = new JTable(model_1);
				scrollPane_1.setViewportView(table_1);
			}
		}
		{
			chckbxExcludeBB = new JCheckBox("Exclude Baseband");
			chckbxExcludeBB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						filelist();
					}
					catch (Exception e) {}
				}
			});
			{
				chckbxExcludeSystem = new JCheckBox("Exclude system");
				chckbxExcludeSystem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							filelist();
						}
						catch (Exception e) {}
					}
				});
				{
					chckbxWipeCache = new JCheckBox("Wipe Cache");
					chckbxWipeCache.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								filelist();
							}
							catch (Exception e) {}
						}
					});
					chckbxWipeCache.setSelected(true);
					contentPanel.add(chckbxWipeCache, "6, 6");
				}

				contentPanel.add(chckbxExcludeSystem, "6, 8, left, top");
			}
			contentPanel.add(chckbxExcludeBB, "6, 10");
		}
		{
			chckbxExcludeKernel = new JCheckBox("Exclude Kernel");
			chckbxExcludeKernel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						filelist();
					}
					catch (Exception e) {}
				}
			});
			contentPanel.add(chckbxExcludeKernel, "6, 12");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						retcode=true;
						dispose();
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
		doRefreshTables();
		setLanguage();
	}
	
	public Bundle getBundle() throws IOException {
		setVisible(true);
		if (retcode) {
			if (GlobalConfig.getProperty("bundle").equals("file")) {
				MyLogger.getLogger().debug("Choosed bundle "+result);
			}
			else {
				selected=new Bundle(result,Bundle.FOLDERTYPE);
				selected.setDevice((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 1));
				selected.setVersion((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 2));
				selected.setBranding((String)modelFirm.getValueAt(tableFirm.getSelectedRow(), 3));
			}
			selected.setWipeData(chckbxWipeUserdata.isSelected());
			selected.setWipeCache(chckbxWipeCache.isSelected());
			selected.setExcludeSystem(chckbxExcludeSystem.isSelected());
			selected.setExcludeKernel(chckbxExcludeKernel.isSelected());
			selected.setExcludeBB(chckbxExcludeBB.isSelected());
			MyLogger.getLogger().info("Selected "+result);
			return selected;
		}
		return null;
	}

	public void setLanguage() {
		Language.translate(this);
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
	    	doRefreshTables();
	    }
	}

	public void doRefreshTables() {
		try {
			dirlist();
			filelist();
		}
		catch (Exception e) {}		
	}
}