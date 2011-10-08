package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lang.Language;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProfileSaveAs extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;


	/**
	 * Create the dialog.
	 */
	public ProfileSaveAs() {
		setName("ProfileSaveAs");
		setModal(true);
		setResizable(false);
		setTitle("Save Profile As");
		setBounds(100, 100, 450, 142);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblProfileName = new JLabel("Profile Name :");
			lblProfileName.setName("lblProfileName");
			contentPanel.add(lblProfileName, "2, 2");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "2, 4, fill, default");
			textField.setColumns(10);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				textField.setText("");
				dispose();
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (textField.getText().length()==0) {
							JOptionPane.showMessageDialog(null, "The field must not be empty");
						}
						else dispose();
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
						textField.setText("");
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLanguage();
	}

	public boolean SaveAs(X10Apps apps) {
		setVisible(true);
		if (textField.getText().length()>0)
			apps.saveProfile(textField.getText());
		return textField.getText().length()>0;
	}
	
	public void setLanguage() {
		Language.translate(this);
	}

}
