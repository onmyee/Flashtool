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
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JList;

import org.lang.Language;

public class EncDecGUI extends JDialog {

	/**
	 * 
	 */
	public class MyFile extends File {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyFile(String path) {
			super(path);
		}
		
		public String toString() {
			return getName();
		}
		
		public MyFile[] listFiles() {
			File f[] = super.listFiles();
			Vector<MyFile> v = new Vector<MyFile>();
			for (int i =0;i<f.length;i++) {
				MyFile mf = new MyFile(f[i].getAbsolutePath());
				v.add(mf);
			}
			MyFile[] list = new MyFile[v.size()];
			v.toArray(list);
			return list;
		}
	}
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	JList listFiles;
	JList listToConvert;
	DefaultListModel listFilesModel = new DefaultListModel();
	DefaultListModel listToConvertModel = new DefaultListModel();
	SortedSizeListModel sortedFilesModel = new SortedSizeListModel(listFilesModel);
	SortedSizeListModel sortedToConvertModel = new SortedSizeListModel(listToConvertModel);

	
	/**
	 * Create the dialog.
	 */
	public EncDecGUI() {
		setName("EncDecGUI");
		setResizable(false);
		setModal(true);
		setTitle("Decryption wizard");
		setBounds(100, 100, 615, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("51dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(48dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("42dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("53dlu:grow"),},
			new RowSpec[] {
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
				RowSpec.decode("max(24dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listToConvertModel.removeAllElements();
				dispose();
			}
		});
		{
			JLabel lblSourceFolder = new JLabel("Source folder :");
			lblSourceFolder.setName("lblSourceFolder");
			contentPanel.add(lblSourceFolder, "2, 2, 3, 1, left, default");
		}
		{
			textField = new JTextField();
			textField.setEditable(false);
			contentPanel.add(textField, "2, 4, 7, 1, fill, default");
			textField.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doChoose();
				}
			});
			contentPanel.add(button, "10, 4, right, center");
		}
		{
			JLabel lblFiles = new JLabel("Files");
			lblFiles.setName("lblFiles");
			contentPanel.add(lblFiles, "2, 8, 3, 1, center, default");
		}
		{
			JLabel lblFilesToConvert = new JLabel("Files to convert");
			lblFilesToConvert.setName("lblFilesToConvert");
			contentPanel.add(lblFilesToConvert, "8, 8, 3, 1, center, default");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 10, 3, 7, fill, fill");
			{
				listFiles = new JList();
				listFiles.setModel(sortedFilesModel);
				scrollPane.setViewportView(listFiles);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "8, 10, 3, 7, fill, fill");
			{
				listToConvert = new JList();
				listToConvert.setModel(sortedToConvertModel);
				scrollPane.setViewportView(listToConvert);
			}
		}
		{
			JButton button = new JButton("->");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] values = listFiles.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listToConvertModel.addElement(values[i]);
						listFilesModel.removeElement(values[i]);
					}	
				}
			});
			contentPanel.add(button, "6, 12, center, center");
		}
		{
			JButton button = new JButton("<-");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] values = listToConvert.getSelectedValues();
					for (int i=0;i<values.length;i++) {
						listToConvertModel.removeElement(values[i]);
						listFilesModel.addElement(values[i]);
					}
				}
			});
			contentPanel.add(button, "6, 14, center, center");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
						listToConvertModel.removeAllElements();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLanguage();
	}

	public void setLanguage() {
		Language.translate(this);
	}
	
	public void doChoose() {
		JFileChooser chooser = new JFileChooser();
		if (textField.getText().length()==0)
			chooser.setCurrentDirectory(new java.io.File("."));
		else
			chooser.setCurrentDirectory(new java.io.File(textField.getText()));
	    chooser.setDialogTitle("Choose a folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	textField.setText(chooser.getSelectedFile().getAbsolutePath());
	    	dirlist();
	    }
	}

	class MyFilter implements FilenameFilter {
		String filter;
		
		public MyFilter(String lfilter) {
			filter = lfilter;
		}
		
		public boolean accept(File dir, String name) {
			return (name.toUpperCase().endsWith(filter.toUpperCase()));
		}
	}

	private void dirlist() {
		listFilesModel.removeAllElements();
		listToConvertModel.removeAllElements();
	    MyFile dir = new MyFile(textField.getText());
	    MyFile[] chld=dir.listFiles();
	    for(int i = 0; i < chld.length; i++){
	    	if (chld[i].isFile())
	    		listFilesModel.addElement((MyFile)chld[i]);
	    }
	    if (chld.length>0) {
	    	listFiles.setSelectionInterval(0, 0);
	    }
	}
	
	public Object[] getList() {
		if (listToConvertModel.getSize()>0) {
			listToConvert.setSelectionInterval(0,listToConvertModel.getSize()-1);
			return listToConvert.getSelectedValues();
		}
		else return null;
	}

}
