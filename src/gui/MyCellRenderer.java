package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Color blue = new Color(9,87,171);
	public static Color red = new Color(161,27,50);
    
	public MyCellRenderer() {
    	setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Set the text and color 
        //background for rendering
    	ListItem li = (ListItem)value;
        setText(li.getName());

        if (isSelected) {
        	if (li.fileExists()) {
        		setBackground(blue);
        		setBorder(BorderFactory.createLineBorder(blue, 2));
        	}
        	else {
        		setBackground(red);
        		setBorder(BorderFactory.createLineBorder(red, 2));
        	}
            setForeground(Color.WHITE);
            
        }
        else {
        	if (li.fileExists()) {
        		setForeground(li.getForeground());
        		setBackground(li.getBackground());
        		setBorder(BorderFactory.createLineBorder(list.getBackground(), 2));
        	}
        	else {
        		setForeground(Color.red);
        		setBackground(li.getBackground());
        		setBorder(BorderFactory.createLineBorder(list.getBackground(), 2));        		
        	}
        }
        return this;
    
    }

}
