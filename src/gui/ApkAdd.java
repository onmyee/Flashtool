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
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ApkAdd extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField apkpath;
	private JTextField apkdesc;
	private JDialog root=this;


	/**
	 * Create the dialog.
	 */
	public ApkAdd(String papkpath, String papkdesc) {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Add APK to /system/app");
		setBounds(100, 100, 450, 146);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblPathToApk = new JLabel("Path to APK :");
			contentPanel.add(lblPathToApk, "2, 2, right, default");
		}
		{
			apkpath = new JTextField();
			if (papkpath.length()>0) {
				apkpath.setEditable(false);
				apkpath.setText(papkpath);
			}
			contentPanel.add(apkpath, "4, 2, fill, default");
			apkpath.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			if (!apkpath.isEditable()) button.setEnabled(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser(); 
					if (apkpath.getText().length()==0)
						chooser.setCurrentDirectory(new java.io.File("."));
					else
						chooser.setCurrentDirectory(new java.io.File(apkpath.getText()));
				    chooser.setDialogTitle("Choose a folder");
				    FileFilter filter1 = new ExtensionFileFilter("APK", new String[] { "APK" });
				    chooser.setFileFilter(filter1);
				    //
				    // disable the "All files" option.
				    //
				    chooser.setAcceptAllFileFilterUsed(false);
				    //    
				    if (chooser.showOpenDialog(root) == JFileChooser.APPROVE_OPTION) {
				    	apkpath.setText(chooser.getSelectedFile().getAbsolutePath());
				    }
				}
			});
			contentPanel.add(button, "6, 2");
		}
		{
			JLabel lblDescription = new JLabel("Description :");
			contentPanel.add(lblDescription, "2, 4, right, default");
		}
		{
			apkdesc = new JTextField();
			apkdesc.setText(papkdesc);
			contentPanel.add(apkdesc, "4, 4, fill, default");
			apkdesc.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						apkpath.setText("");
						apkdesc.setText("");
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String[] getEntry() {
		setVisible(true);
		return new String[] {apkpath.getText(),apkdesc.getText()};
	}
	
	class ExtensionFileFilter extends FileFilter {
		  String description;

		  String extensions[];

		  public ExtensionFileFilter(String description, String extension) {
		    this(description, new String[] { extension });
		  }

		  public ExtensionFileFilter(String description, String extensions[]) {
		    if (description == null) {
		      this.description = extensions[0];
		    } else {
		      this.description = description;
		    }
		    this.extensions = (String[]) extensions.clone();
		    toLower(this.extensions);
		  }

		  private void toLower(String array[]) {
		    for (int i = 0, n = array.length; i < n; i++) {
		      array[i] = array[i].toLowerCase();
		    }
		  }

		  public String getDescription() {
		    return description;
		  }

		  public boolean accept(File file) {
		    if (file.isDirectory()) {
		      return true;
		    } else {
		      String path = file.getAbsolutePath().toLowerCase();
		      for (int i = 0, n = extensions.length; i < n; i++) {
		        String extension = extensions[i];
		        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
		          return true;
		        }
		      }
		    }
		    return false;
		  }
		}
}
