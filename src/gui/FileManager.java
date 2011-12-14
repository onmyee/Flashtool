package gui;

import java.util.HashSet;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JScrollPane;
import javax.swing.JList;
import org.adb.AdbUtility;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileManager extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	DefaultListModel listFilesModel = new DefaultListModel();
	JList list=null;
	JLabel lblCurPath;
	private JButton btnUp;

	/**
	 * Create the frame.
	 */
	public FileManager() throws Exception {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!lblCurPath.getText().equals("/")) {
					lblCurPath.setText(lblCurPath.getText().substring(0, lblCurPath.getText().lastIndexOf('/')));
					lblCurPath.setText(lblCurPath.getText().substring(0, lblCurPath.getText().lastIndexOf('/'))+"/");
					try {
						fillList();
					}
					catch (Exception e2) {}
				}
			}
		});
		contentPane.add(btnUp, "4, 2, left, default");
		
		lblCurPath = new JLabel("/");
		contentPane.add(lblCurPath, "4, 4");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "4, 6, fill, fill");
		
		list = new JList();
		list.setFixedCellWidth(200);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (e.getClickCount()==2) {
						lblCurPath.setText(lblCurPath.getText()+list.getSelectedValue()+"/");
						fillList();
					}
				}
				catch (Exception e1) {}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(listFilesModel);
		scrollPane.setViewportView(list);
		fillList();
	}
	
	public void fillList() throws Exception {
		listFilesModel.removeAllElements();
		HashSet<String> result;
		Iterator<String> i;
		result = AdbUtility.ls(lblCurPath.getText(),"d");
		i = result.iterator();
		while (i.hasNext()) {
			String val = i.next();
			listFilesModel.addElement(val);
		}
		result = AdbUtility.ls(lblCurPath.getText(),"f");
		i = result.iterator();
		while (i.hasNext())
			listFilesModel.addElement(i.next());
	}

}
