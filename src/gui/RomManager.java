package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.io.File;
import org.lang.Language;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextField;
import javax.swing.JList;


public class RomManager extends JDialog {

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
	DefaultListModel listPhoneModel = new DefaultListModel();
	DefaultListModel listPCModel = new DefaultListModel();
	boolean retcode=false;

	private void dirlist() {
		listFolder.setModel(listPhoneModel);
		listPhoneModel.removeAllElements();
		listPCModel.removeAllElements();
		File dir = new File(folderSource.getText());
		File[] list = dir.listFiles();
		for (int i=0;i<list.length;i++) {
			if (list[i].isFile() && list[i].getName().toUpperCase().endsWith("ZIP"))
				listPhoneModel.addElement(list[i].getName());
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public RomManager() {
		setName("rommanager");
		setTitle("CustomRom Manager");
		setModal(true);
		setBounds(100, 100, 583, 357);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(148dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(109dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(105dlu;default)"),},
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
					ColumnSpec.decode("max(223dlu;default)"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblSelectSourceFolder = new JLabel("Select source folder :");
				panel.add(lblSelectSourceFolder, "2, 2, 3, 1");
			}
			{
				folderSource  = new JTextField();
				folderSource.setEditable(false);
				folderSource.setText("./CustomRoms/");
				dirlist();
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
					FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
		}
		{
			JLabel lblPCList = new JLabel("Rom's on PC :");
			contentPanel.add(lblPCList, "2, 4");
		}
		{
			JLabel lblPhoneContent = new JLabel("Rom's on Phone :");
			contentPanel.add(lblPhoneContent, "6, 4");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 6, 1, 5, fill, fill");
			{
				listFolder = new JList();
				scrollPane.setViewportView(listFolder);
			}
		}
		{
			JButton button = new JButton("->");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listFolder.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listPCModel.addElement(values[i]);
						listPhoneModel.removeElement(values[i]);
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
				listFirmware.setModel(listPCModel);
				scrollPane.setViewportView(listFirmware);
			}
		}
		{
			JButton button = new JButton("<-");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = listFirmware.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listPCModel.removeElement(values[i]);
						listPhoneModel.addElement(values[i]);
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
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//Add action here :D
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
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
//		setLanguage();
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

}