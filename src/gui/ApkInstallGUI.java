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


public class ApkInstallGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JButton okButton;
	private JTextField folderSource;
	private JList listFolder;
	DefaultListModel listFolderModel = new DefaultListModel();
	SortedNameListModel sortedListFolderModel = new SortedNameListModel(listFolderModel);

	/**
	 * A class that implements the Java FileFilter interface.
	 */
	private void dirlist() {
		listFolderModel.removeAllElements();
		File[] list = (new File(folderSource.getText())).listFiles(new ApkFileFilter());
		for (int i=0;i<list.length;i++) {
			listFolderModel.addElement(list[i].getName());
		}
	}
	
	public ApkInstallGUI(String folder) {
		init();
		folderSource.setText(folder);
		dirlist();
	}

	public ApkInstallGUI() {
		init();
	}
	/**
	 * Create the dialog.
	 */
	public void init() {
		setResizable(false);
		setName("ApkInstallGUI");
		setTitle("APK Installer");
		setModal(true);
		setBounds(100, 100, 600, 357);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(127dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(217dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(19dlu;default):grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(9dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default)"),
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
			JLabel lblSelectSourceFolder = new JLabel("Select source folder :");
			lblSelectSourceFolder.setName("lblSelectSourceFolder");
			contentPanel.add(lblSelectSourceFolder, "2, 2");
			//lblSelectSourceFolder.setName("lblSelectSourceFolder");
		}
		{
			folderSource  = new JTextField();
			contentPanel.add(folderSource, "2, 4, 3, 1");
			folderSource.setEditable(false);
			folderSource.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			contentPanel.add(button, "6, 4");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					doChoose();
				}
			});
		}
		{
			JLabel lblFirmwareList = new JLabel("Folder list :");
			lblFirmwareList.setName("lblFirmwareList");
			contentPanel.add(lblFirmwareList, "2, 6");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 8, 5, 5, fill, fill");
			{
				listFolder = new JList();
				listFolder.setModel(sortedListFolderModel);
				scrollPane.setViewportView(listFolder);
			}
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				folderSource.setText("");
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
	
	public String getFolder() {
		setVisible(true);
		return folderSource.getText();
	}

}