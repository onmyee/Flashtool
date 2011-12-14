package org.lang;

import gui.XMLFile;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.system.PropertiesFile;

public final class Language {

	private static String lang =null;;
	private static Properties langs = new Properties();
	private static XMLFile langdef;

	public static void Init(String plang) throws Exception {
		if (langdef==null) {
			 langdef = new XMLFile(new File("./x10flasher_lib/langs/Languages.xml"));
			 Enumeration<String> e = langdef.getLanguages();
			 while(e.hasMoreElements()) {
				 String plang1=e.nextElement();
				 langs.put(plang1, new PropertiesFile("","./x10flasher_lib/langs/"+plang1+".properties"));
			 }			
		}
		lang = plang;
	}

	public static Enumeration<String> getMenuEntries() {
		return langdef.getMenuEntries(lang);
	}
	
	public static String getMenuItem(String menuitem) {
		return langdef.getMenuEntry(lang, menuitem);
	}
	
	public static Enumeration<String> getLanguages() {
		return langdef.getLanguages();
	}
	
	public static String getMessage(String id) {
		String res = ((PropertiesFile)langs.get(lang.toLowerCase())).getProperty(id);
		if (res == null) return "";
		return res;
	}
	
	public static PropertiesFile getProperties(String plang) {
		return (PropertiesFile)langs.get(plang);
	}
	
	public static Enumeration<Object> list() {
		return langs.keys();
	}
	
	public static void translate (Container root) {
		Component[] components;
		
		if (root instanceof JMenu)
			components = ((JMenu)root).getMenuComponents();
		else components = root.getComponents();
		
	    for (Component com : components) {
	        if (com instanceof Container) {
	        	translate((Container)com);
	        }
	    }
	    
	    if (root.getName() != null) {
	    	if (root instanceof JMenu)
	    		((JMenu) root).setText(Language.getMessage(root.getName()));
	    	if (root instanceof JMenuItem) {
	    		if (!((JMenuItem) root).getName().startsWith("rdbtnmntm"))
	    				((JMenuItem) root).setText(Language.getMessage(root.getName()));
	    	}
	    	if (root instanceof JButton)
	    		((JButton) root).setText(Language.getMessage(root.getName()));
			if (root instanceof JLabel)
				((JLabel)root).setText(Language.getMessage(root.getName()));			
	    	if (root instanceof JFrame)
				((JFrame)root).setTitle(Language.getMessage(root.getName()+"_title"));			
			if (root instanceof JDialog)
				((JDialog)root).setTitle(Language.getMessage(root.getName()+"_title"));
			if (root instanceof JCheckBox)
				((JCheckBox)root).setText(Language.getMessage(root.getName()));
	    }		
	}
}
