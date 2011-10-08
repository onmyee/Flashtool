package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomAppList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableCustom;
	private DefaultTableModel modelCustom;
	X10Apps _apps;
	String result="";
	JButton okButton;


	/**
	 * Create the dialog.
	 */
	public CustomAppList(X10Apps apps) {
		setResizable(false);
		setModal(true);
		_apps = apps;
		setTitle("Custom Application List");
		setBounds(100, 100, 396, 334);
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
				tableCustom = new JTable() {
				    public boolean isCellEditable(int rowIndex, int vColIndex) {
				        return false;
				    }
				};
				tableCustom.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if (arg0.getClickCount()==2) {
							result = (String)tableCustom.getModel().getValueAt(tableCustom.getSelectedRow(), 0);
							dispose();
						}
						if (!okButton.isEnabled() && tableCustom.getSelectedRow()>0) okButton.setEnabled(true);
					}
				});
				tableCustom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(tableCustom);
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
						result = (String)tableCustom.getModel().getValueAt(tableCustom.getSelectedRow(), 0);
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
						result = "";
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		fillLists();
	}

	private void fillLists() {
		modelCustom = new DefaultTableModel();
		tableCustom.setModel(modelCustom);
		modelCustom.addColumn("APK Name");
		modelCustom.addColumn("Presentation Name");
		Iterator i1 = _apps.customList().keySet().iterator();
		while (i1.hasNext()) {
			String apk = (String)i1.next();
			String row[] = {apk,_apps.getRealName(apk)};
			modelCustom.addRow(row);
		}
		if (modelCustom.getRowCount()>0)
			tableCustom.setRowSelectionInterval(0, 0);
		else okButton.setEnabled(false);
	}

	public String getApp() {
		setVisible(true);
		return result;
	}
}
