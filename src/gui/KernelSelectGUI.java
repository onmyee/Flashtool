package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.logger.MyLogger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class KernelSelectGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private String result = "";
	private DefaultTableModel modelVersion;
	private JButton okButton;
	private JButton cancelButton;


	/**
	 * Create the dialog.
	 */
	public KernelSelectGUI(String root) {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				result="";
				dispose();
			}
		});
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Kernel Selector");
		setBounds(100, 100, 200, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 2, fill, fill");
			{
				table = new JTable() {
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int rowIndex, int vColIndex) {
				        return false;
				    }
				};
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(table);
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
				    	result=(String)modelVersion.getValueAt(table.getSelectedRow(), 0);
				    	dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result="";
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		try {
			dirlist(root);
		}
		catch (Exception e) {
		}
	}
	
	public String getVersion() {
		setVisible(true);
		return result;
	}

	private void dirlist(String root) throws Exception{
		boolean hasElements = false;
		modelVersion = new DefaultTableModel();
		modelVersion.addColumn("Version");
		table.setModel(modelVersion);
	    	File dir = new File("./devices/"+root+"/kernel");
		    File[] chld = dir.listFiles();
		    for(int i = 0; i < chld.length; i++){
		    	if (chld[i].isDirectory()) {
		    			hasElements = true;
		    			modelVersion.addRow(new String[]{chld[i].getName()});
		    			MyLogger.getLogger().debug("Adding "+chld[i].getName()+" to list of firmwares");
		    	}
		    }
	    if (!hasElements) {
	    	okButton.setEnabled(false);
	    	result="";
	    }
	    else {
	    	table.setRowSelectionInterval(0, 0);
	    	result=(String)modelVersion.getValueAt(table.getSelectedRow(), 0);
	    	okButton.setEnabled(true);
	    }
	}

}
