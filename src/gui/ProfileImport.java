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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ListSelectionModel;

import org.lang.Language;
import org.logger.MyLogger;
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


public class ProfileImport extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String fsep = OS.getFileSeparator();
	private final JPanel contentPanel = new JPanel();
	private JTable tableProfiles;
	private DefaultTableModel tableProfilesmodel;
	private XTableColumnModel tableProfilesColumnModel = new XTableColumnModel();
	private JTable tableContent;
	private DefaultTableModel model_1; 
	private String result;
	JButton okButton;
	private JTextField folderSource;

	private void dirlist() throws Exception{
		boolean hasElements = false;
		tableProfilesmodel = new DefaultTableModel();
		tableProfilesmodel.addColumn("File");
		tableProfilesmodel.addColumn("Profile Name");
		tableProfiles.setModel(tableProfilesmodel);
		tableProfiles.setColumnModel(tableProfilesColumnModel);
		tableProfiles.createDefaultColumnsFromModel();
		tableProfilesColumnModel.setColumnVisible(tableProfilesColumnModel.getColumnByModelIndex(0), false);

		File dir = new File(folderSource.getText());
		File[] chld = dir.listFiles();
		for(int i = 0; i < chld.length; i++){
		 	if (chld[i].isFile() && chld[i].getName().toLowerCase().endsWith("ftp")) {
		    	hasElements = true;
	    		JarFile jf = new JarFile(chld[i]);
	    		tableProfilesmodel.addRow(new String[]{chld[i].getName(),jf.getManifest().getMainAttributes().getValue("profileName")});
	    		MyLogger.debug("Adding "+chld[i].getName()+" to list of profiles");
		    }
		}	    	
	    if (!hasElements)
	    	okButton.setEnabled(false);
	    else {
	    	tableProfiles.setRowSelectionInterval(0, 0);
	    	result=(String)tableProfilesmodel.getValueAt(tableProfiles.getSelectedRow(), 0);
	    }
		filelist();
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
	    	folderSource.setText(chooser.getSelectedFile().getPath());
	    	try {
	    	dirlist();
	    	} catch (Exception e) {}
	    }
	}

	public void filelist() throws Exception {
		boolean hasElements = false;
		if (tableProfiles.getSelectedRow()>-1) {
			model_1 = new DefaultTableModel();
			model_1.addColumn("File");
			tableContent.setModel(model_1);
			String file = (String)tableProfilesmodel.getValueAt(tableProfiles.getSelectedRow(),0);
			JarFile f = new JarFile(folderSource.getText()+"/"+file);
				Enumeration<JarEntry> e = f.entries();
				while (e.hasMoreElements()) {
			    	hasElements = true;
			    	JarEntry el = e.nextElement();
			    	if (!el.getName().startsWith("META")) {
			    		model_1.addRow(new String[]{el.getName()});
			    		MyLogger.debug("Adding "+el.getName()+" to the content of "+result);
			    	}
				}
			if (hasElements)
				tableContent.setRowSelectionInterval(0, 0);
		}
		else {
			model_1 = new DefaultTableModel();
			model_1.addColumn("File");
			tableContent.setModel(model_1);			
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public ProfileImport(String Folder) {
		setResizable(false);
		setName("firmSelect");
		setTitle("Profile Import");
		setModal(true);
		setBounds(100, 100, 527, 342);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(127dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(62dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("15dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				folderSource.setText("");
				dispose();
			}
		});
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "2, 2, 3, 1, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("max(294dlu;default)"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("max(35dlu;default):grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblSelectFolder = new JLabel("Folder Source :");
				panel.add(lblSelectFolder, "1, 2, 2, 1");
			}
			{
				folderSource = new JTextField();
				folderSource.setEditable(false);
				folderSource.setText(Folder);
				panel.add(folderSource, "1, 4, 2, 1, fill, default");
				folderSource.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						doChoose();
					}
				});
				panel.add(button, "4, 4");
			}
		}
		{
			JLabel lblSelectFirmware = new JLabel("Select Profile");
			lblSelectFirmware.setName(getName()+"_"+"lblSelectFirmware");
			contentPanel.add(lblSelectFirmware, "2, 4, left, fill");
		}
		{
			{
				model_1 = new DefaultTableModel();
				model_1.addColumn("File");
			}
		}
		{
			JLabel lblFilesInThis = new JLabel("Profile Content");
			lblFilesInThis.setName(getName()+"_"+"lblFilesInThis");
			contentPanel.add(lblFilesInThis, "4, 4, left, fill");
		}
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "2, 6, left, fill");
		tableProfiles = new JTable() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int vColIndex) {
		        return false;
		    }
		};
		tableProfiles.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				result=(String)tableProfilesmodel.getValueAt(tableProfiles.getSelectedRow(), 0);
				try {
					filelist();
				}
				catch (Exception e) {}
			}
		});
		tableProfiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				result=(String)tableProfilesmodel.getValueAt(tableProfiles.getSelectedRow(), 0);
				try {
					filelist();
				}
				catch (Exception e) {}
			}
		});
		tableProfiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tableProfiles);
		{
			JScrollPane scrollPane_1 = new JScrollPane();
			contentPanel.add(scrollPane_1, "4, 6, fill, fill");
			{
				tableContent = new JTable(model_1);
				scrollPane_1.setViewportView(tableContent);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
						folderSource.setText("");
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		try {
			dirlist();
			filelist();
		}
		catch (Exception e) {}
		//setLanguage();
	}
	
	public String getProfile() {
		setVisible(true);
		if (folderSource.getText().length()>0) {
			return folderSource.getText()+"/"+result;
		}
		return "";
	}

	public void setLanguage() {
		Language.translate(this);
	}

}