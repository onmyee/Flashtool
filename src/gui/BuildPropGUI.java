package gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.adb.AdbUtility;
import org.apache.commons.io.IOUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchEngine;
import org.lang.Language;
import org.logger.MyLogger;
import org.system.Devices;
import org.system.GlobalConfig;
import org.system.OS;
import org.system.Shell;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BuildPropGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static String fsep = OS.getFileSeparator();
	private File f;
	//private EditTextArea textArea;
	private RSyntaxTextArea textArea;
	private JTextField textPath;
	private JTextField textFile;
	private String localfile;
	private JButton btnSave;
	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;
	   
	public void loadFile(JTextArea area) {
		try {
			if (textPath.getText().endsWith("/")) {
				textPath.setText(textPath.getText().substring(0, textPath.getText().length()-1));
			}
			localfile = Devices.getCurrent().getWorkDir()+fsep+textFile.getText();
			AdbUtility.pull(textPath.getText()+"/"+textFile.getText(), Devices.getCurrent().getWorkDir()+fsep+textFile.getText());
			f = new File(localfile);
            FileReader fr = new FileReader(f);
            area.read(fr, null);
            area.setCaretPosition(0);
            fr.close();
            btnSave.setEnabled(true);
            setTitle(textPath.getText()+"/"+textFile.getText());
        }
        catch (Exception e) {
        	MyLogger.getLogger().error(e.getMessage());
        }
    }

	public void writeFile(JTextArea area) {
		try {
			FileOutputStream fout = new FileOutputStream(f);
			IOUtils.write(area.getText(), fout, "ISO-8859-1");
			fout.close();
		}
		catch (IOException exception) {
			MyLogger.getLogger().error(exception.getMessage());
		}
		finally {
				try {
					if (AdbUtility.Sysremountrw()) {
						AdbUtility.push(localfile,GlobalConfig.getProperty("deviceworkdir")+"/"+textFile.getText()+"_new");
						String stat = AdbUtility.getFilePerms(textPath.getText()+"/"+textFile.getText());
						int access = stat.indexOf("Access:");
						int uid = stat.indexOf("Uid:");
						int gid = stat.indexOf("Gid:");
						String saccess = stat.substring(access, uid);
						String suid = stat.substring(uid, gid);
						String sgid = stat.substring(gid);
						int astart = saccess.indexOf("(")+1;
						int aend = saccess.indexOf("/");
						int ustart = suid.indexOf("(")+1;
						int uend = suid.indexOf("/");
						int gstart = sgid.indexOf("(")+1;
						int gend = sgid.indexOf("/");
						Shell shell = new Shell("pushfile");
						shell.setProperty("VFILE", textFile.getText());
						shell.setProperty("VPATH", textPath.getText());
						shell.setProperty("VUID", suid.substring(ustart, uend).trim());
						shell.setProperty("VGID", sgid.substring(gstart, gend).trim());
						shell.setProperty("VPERM", saccess.substring(astart, aend).trim());
						shell.runRoot();
					}
					else MyLogger.getLogger().info("Error mounting /system rw");
				}
				catch (Exception exception) {
					MyLogger.getLogger().error(exception.getMessage());
				}
			}
		}

	/**
	 * Create the frame.
	 */
	public BuildPropGUI() {
		setTitle("File Editor");
		setName("BuildPropGUI");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 549, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(25dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//if (f!=null) f.delete();
				dispose();
			}
		});
		

		JLabel lblPath = new JLabel("path:");
		contentPane.add(lblPath, "2, 2, right, default");
		
		textPath = new JTextField();
		textPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				textArea.setText("");
				btnSave.setEnabled(false);
				if (f!=null)
					f.delete();
			}
		});
		contentPane.add(textPath, "4, 2, fill, center");
		textPath.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("File :");
		contentPane.add(lblNewLabel, "6, 2, right, default");
		
		textArea = new RSyntaxTextArea(25, 70);
		
		textFile = new JTextField();
		textFile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				textArea.setText("");
				btnSave.setEnabled(false);
				if (f!=null)
					f.delete();
			}
		});
		contentPane.add(textFile, "8, 2, fill, center");
		textFile.setColumns(10);
		
				JToolBar toolBar = new JToolBar();
				contentPane.add(toolBar, "2, 4, 7, 1");
				searchField = new JTextField(30);
				toolBar.add(searchField);
				JButton b = new JButton("Find Next");
				b.setActionCommand("FindNext");
				b.addActionListener(this);
				toolBar.add(b);
				b = new JButton("Find Previous");
				b.setActionCommand("FindPrev");
				b.addActionListener(this);
				toolBar.add(b);
				regexCB = new JCheckBox("Regex");
				toolBar.add(regexCB);
				matchCaseCB = new JCheckBox("Match Case");
				toolBar.add(matchCaseCB);
		RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		contentPane.add(scrollPane, "2, 6, 7, 19, fill, fill");

		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.setName("btnSave");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeFile(textArea);
			}
		});
		contentPane.add(btnSave, "2, 26");
		
		JButton btnReload = new JButton("Reload");
		btnReload.setName("btnReload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFile(textArea);
			}
		});
		contentPane.add(btnReload, "4, 26");
		
		JButton btnClose = new JButton("Close");
		btnClose.setName("btnClose");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if (f!=null) f.delete();
				dispose();
			}
		});
		contentPane.add(btnClose, "8, 26, right, default");
		//loadFile(textArea);
		setLanguage();
	}

	public void setLanguage() {
		Language.translate(this);
	}

	public void actionPerformed(ActionEvent e) {

	      String command = e.getActionCommand();

	      if ("FindNext".equals(command)) {
	         String text = searchField.getText();
	         if (text.length() == 0) {
	            return;
	         }
	         boolean forward = true;
	         boolean matchCase = matchCaseCB.isSelected();
	         boolean wholeWord = false;
	         boolean regex = regexCB.isSelected();
	         boolean found = SearchEngine.find(textArea, text, forward,
	               matchCase, wholeWord, regex);
	         if (!found) {
	            JOptionPane.showMessageDialog(this, "Text not found");
	         }
	      }

	      else if ("FindPrev".equals(command)) {
	         String text = searchField.getText();
	         if (text.length() == 0) {
	            return;
	         }
	         boolean forward = false;
	         boolean matchCase = matchCaseCB.isSelected();
	         boolean wholeWord = false;
	         boolean regex = regexCB.isSelected();
	         boolean found = SearchEngine.find(textArea, text, forward,
	               matchCase, wholeWord, regex);
	         if (!found) {
	            JOptionPane.showMessageDialog(this, "Text not found");
	         }
	      }

	   }

}
