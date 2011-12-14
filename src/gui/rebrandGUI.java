package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import org.system.Devices;
import org.system.OS;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Enumeration;
import java.io.File;

public class rebrandGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static String fsep = OS.getFileSeparator();
	private String result="";
	private static XMLRebrand rebrand;
	private JComboBox comboBoxId;
	private JComboBox comboBoxType;
	private String _curId;

	/**
	 * Create the dialog.
	 */
	public rebrandGUI(String curId) {
		try {
			rebrand = new XMLRebrand(new File("."+fsep+"devices"+fsep+Devices.getCurrent().getId()+fsep+"rebrand"+fsep+"rebrand.xml"));
		}
		catch (Exception e) {
			rebrand = null;
		}
		_curId=curId;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Brand Changer");
		setBounds(100, 100, 450, 157);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				result="";
				dispose();
			}
		});
		{
			JLabel lblNewLabel_1 = new JLabel("Type :");
			contentPanel.add(lblNewLabel_1, "4, 2, right, default");
		}
		{
			comboBoxId = new JComboBox();
			contentPanel.add(comboBoxId, "6, 4, fill, default");
		}
		{
			comboBoxType = new JComboBox();
			comboBoxType.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (arg0.getStateChange()==ItemEvent.SELECTED) {
						comboBoxId.removeAllItems();
						String current = (String)comboBoxType.getSelectedItem();
						Enumeration<String> e = rebrand.getBrands(current);
						int index = 0;
						while (e.hasMoreElements()) {
							String key = e.nextElement();
							comboBoxId.addItem(key);
							if (_curId.equals(rebrand.getId(current, key)))
									comboBoxId.setSelectedIndex(index);
							index++;
						}
					}
				}
			});
			Enumeration<String> e = rebrand.getDevices();
			int index = 0;
			while (e.hasMoreElements()) {
				String elem = e.nextElement();
				comboBoxType.addItem(elem);
				if (rebrand.hasId(elem, curId))
					comboBoxType.setSelectedIndex(index);
				index++;
			}
			contentPanel.add(comboBoxType, "6, 2, fill, default");
		}

		{
			JLabel lblNewLabel = new JLabel("Brand ID :");
			contentPanel.add(lblNewLabel, "4, 4, right, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
							result=rebrand.getId((String)comboBoxType.getSelectedItem(), (String)comboBoxId.getSelectedItem());
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
					public void actionPerformed(ActionEvent e) {
						result="";
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}

	public String getId() {
		setVisible(true);
		return result;
	}

}
