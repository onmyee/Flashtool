package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import flashsystem.X10flash;
import foxtrot.Job;
import foxtrot.Worker;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;

import org.lang.Language;
import org.logger.MyLogger;
import javax.swing.JSpinner;
import javax.swing.ImageIcon;

public class WaitDeviceFlashmodeGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	X10flash _flash;
	boolean cancel;
	boolean result = false;


	/**
	 * Create the dialog.
	 */
	public WaitDeviceFlashmodeGUI(X10flash flash) {
		setName("WaitDeviceGUI_title");
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Flash Mode");
		_flash = flash;
		setBounds(100, 100, 629, 360);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(128dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "2, 2, fill, fill");
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("max(51dlu;default)"),
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("max(59dlu;default)"),}));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, "2, 4, fill, fill");
				panel_1.setLayout(new FormLayout(new ColumnSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC,}));
				{
					JLabel lblUnplug = new JLabel("1 - Unplug the device");
					lblUnplug.setName("lblUnplug");
					panel_1.add(lblUnplug, "2, 2");
				}
				{
					JLabel lblPower = new JLabel("2 - Power off the device");
					lblPower.setName("lblPower");
					panel_1.add(lblPower, "2, 4");
				}
				{
					JLabel lblPress = new JLabel("3 - Press the back button and hold it");
					lblPress.setName("lblPress");
					panel_1.add(lblPress, "2, 6");
				}
				{
					JLabel lblPlug = new JLabel("4 - Plug the USB cable");
					lblPlug.setName("lblPlug");
					panel_1.add(lblPlug, "2, 8");
				}
			}
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(WaitDeviceFlashmodeGUI.class.getResource("/gui/ressources/Unplug_Plug_XFamily.gif")));
			contentPanel.add(label, "4, 2");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setName("cancelButton");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel=true;
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
	
	public boolean deviceFound(JFrame parent) {
		setLocationRelativeTo(parent);
		setVisible(true);
				while (true) {
					if (_flash.deviceFound()) {
						_flash.openDevice();
						result=true;
						break;
					}
					if (cancel) {
						result=false;
						break;
					}
				}
		setVisible(false);
		return result;
	}
}
