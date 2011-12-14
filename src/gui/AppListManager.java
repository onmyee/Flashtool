package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class AppListManager extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable tableX10;
	private JTable tableCustom;
	private DefaultTableModel modelX10;
	private DefaultTableModel modelCustom;
	private X10Apps _apps;

	/**
	 * Create the dialog.
	 */
	public AppListManager(X10Apps apps) {
		_apps = apps;
		setTitle("Application List Manager");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 450, 300);
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
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, "2, 2, fill, fill");
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("X10 Applications", null, panel, null);
				panel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),}));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane, "2, 2, 1, 3, fill, fill");
					{
						tableX10 = new JTable() {
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							public boolean isCellEditable(int rowIndex, int vColIndex) {
						        return false;
						    }
						};
						//tableX10.setAutoCreateColumnsFromModel(false);
						tableX10.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								if (arg0.getClickCount()==2) {
									ApkAdd add = new ApkAdd((String)modelX10.getValueAt(tableX10.getSelectedRow(), 0),(String)modelX10.getValueAt(tableX10.getSelectedRow(), 1));
									String[] result = add.getEntry();
									_apps.modApk(result[0], result[1]);
									fillLists();
								}
							}
						});
						tableX10.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						scrollPane.setViewportView(tableX10);
					}
				}
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Custom Applications", null, panel, null);
				panel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),}));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane, "2, 2, fill, fill");
						JPopupMenu popupMenu = new JPopupMenu();
							JMenuItem mntmAdd = new JMenuItem("Add");
							mntmAdd.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									ApkAdd add = new ApkAdd("","");
									String[] result = add.getEntry();
									if (result[0].length()>0) {
										_apps.addApk(new File(result[0]), result[1]);
										fillLists();
									}
								}
							});
							popupMenu.add(mntmAdd);
							//JMenuItem mntmRemove = new JMenuItem("Remove");
							//popupMenu.add(mntmRemove);
					{
						tableCustom = new JTable() {
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							public boolean isCellEditable(int rowIndex, int vColIndex) {
						        return false;
						    }
						};
						tableCustom.setAutoCreateRowSorter(true);
						tableCustom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						tableCustom.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								if (arg0.getClickCount()==2) {
									ApkAdd add = new ApkAdd((String)modelCustom.getValueAt(tableCustom.getSelectedRow(), 0),(String)modelCustom.getValueAt(tableCustom.getSelectedRow(), 1));
									String[] result = add.getEntry();
									_apps.modApk(result[0], result[1]);
									fillLists();
								}
							}
						});
						scrollPane.setViewportView(tableCustom);
						addPopup(tableCustom, popupMenu);
						addPopup(scrollPane, popupMenu);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Close");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			fillLists();
		}
	}

	private void fillLists() {
		modelX10 = new DefaultTableModel();
		tableX10.setModel(modelX10);
		modelX10.addColumn("APK Name");
		modelX10.addColumn("Presentation Name");
		Iterator<Object> i = _apps.deviceList().keySet().iterator();
		while (i.hasNext()) {
			String apk = (String)i.next();
			String row[] = {apk,_apps.getRealName(apk)};
			modelX10.addRow(row);
		}
		tableX10.setAutoCreateColumnsFromModel(false);
		sortAllRowsBy(modelX10,0,true);
		modelCustom = new DefaultTableModel();
		tableCustom.setModel(modelCustom);
		modelCustom.addColumn("APK Name");
		modelCustom.addColumn("Presentation Name");
		Iterator<Object> i1 = _apps.customList().keySet().iterator();
		while (i1.hasNext()) {
			String apk = (String)i1.next();
			String row[] = {apk,_apps.getRealName(apk)};
			modelCustom.addRow(row);
		}
		tableCustom.setAutoCreateColumnsFromModel(false);
		sortAllRowsBy(modelCustom,0,true);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
	    Vector<?> data = model.getDataVector();
	    Collections.sort(data, new ColumnSorter(colIndex, ascending));
	    model.fireTableStructureChanged();
	}

	// This comparator is used to sort vectors of data
	public class ColumnSorter implements Comparator<Object> {
	    int colIndex;
	    boolean ascending;
	    ColumnSorter(int colIndex, boolean ascending) {
	        this.colIndex = colIndex;
	        this.ascending = ascending;
	    }
	    public int compare(Object a, Object b) {
	        Vector<?> v1 = (Vector<?>)a;
	        Vector<?> v2 = (Vector<?>)b;
	        Object o1 = v1.get(colIndex);
	        Object o2 = v2.get(colIndex);

	        // Treat empty strains like nulls
	        if (o1 instanceof String && ((String)o1).length() == 0) {
	            o1 = null;
	        }
	        if (o2 instanceof String && ((String)o2).length() == 0) {
	            o2 = null;
	        }

	        // Sort nulls so they appear last, regardless
	        // of sort order
	        if (o1 == null && o2 == null) {
	            return 0;
	        } else if (o1 == null) {
	            return 1;
	        } else if (o2 == null) {
	            return -1;
	        } else if (o1 instanceof Comparable) {
	            if (ascending) {
	                return ((Comparable<Object>)o1).compareTo(o2);
	            } else {
	                return ((Comparable<Object>)o2).compareTo(o1);
	            }
	        } else {
	            if (ascending) {
	                return o1.toString().compareTo(o2.toString());
	            } else {
	                return o2.toString().compareTo(o1.toString());
	            }
	        }
	    }
	}

}
