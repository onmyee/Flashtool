package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import flashsystem.Bundle;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import org.lang.Language;
import org.logger.MyLogger;
import org.system.DeviceEntry;
import org.system.Devices;
import org.system.OS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;



public class deviceSelectGui extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String fsep = OS.getFileSeparator();
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private String result="";
	private Bundle _bundle;

	/**
	 * Create the dialog.
	 */
	public deviceSelectGui(Bundle bundle) {
		_bundle = bundle;
		setName("deviceSelectGui");
		setResizable(false);
		setModal(true);
		setTitle("Device Selection");
		setBounds(100, 100, 183, 317);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,}));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				result="";
				dispose();
			}
		});
		
    	Enumeration e = Devices.listDevices(false);
    	int ligne = 4;
    	while (e.hasMoreElements()) {
    		DeviceEntry entry = Devices.getDevice((String)e.nextElement());
    		JRadioButton rdbtnDev = new JRadioButton(entry.getName());
    		rdbtnDev.setName(entry.getId());
    		if (ligne==4) {
    			rdbtnDev.setSelected(true);
    			result = entry.getId();
    		}
    		buttonGroup.add(rdbtnDev);
    		contentPanel.add(rdbtnDev, "4, "+Integer.toString(ligne));
    		ligne = ligne+2;
    		rdbtnDev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result=((JRadioButton)e.getSource()).getName();
				}
			});
    	}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
					public void actionPerformed(ActionEvent e) {
						result ="";
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLanguage();
	}

	public boolean isSelected() {
		setVisible(!_bundle.hasLoader());
		if (!_bundle.hasLoader() && (result.length()>0)) {
			MyLogger.debug("Choosed loader from device selection :"+result);
			if (Devices.getDevice(result).hasUnlockedLoader()) {
				LoaderSelectGUI sel = new LoaderSelectGUI();
				String resultL = sel.getResult();
				if (resultL.equals("L")) 
					_bundle.setLoader(new File(Devices.getDevice(result).getLoader()));
				else 
					_bundle.setLoader(new File(Devices.getDevice(result).getLoaderUnlocked()));
			}
			else
				_bundle.setLoader(new File(Devices.getDevice(result).getLoader()));
		}
		return ((result.length()>0) || _bundle.hasLoader());
	}

	public String getDevice() {
		setVisible(true);
		return result;
	}

	public void setLanguage() {
		Language.translate(this);
	}

}
