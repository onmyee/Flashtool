package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import org.lang.Language;

public class LangActionListener implements ActionListener {
	
	ButtonGroup _bgroup;
	FlasherGUI _f;
	String _lang;
	
	public LangActionListener(String lang, ButtonGroup b, FlasherGUI f) {
		_bgroup=b;
		_lang=lang;
		_f = f;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			Language.Init(_lang);
			Enumeration<AbstractButton> en = _bgroup.getElements();
			while (en.hasMoreElements()) {
				AbstractButton b = en.nextElement();
				b.setText(Language.getMenuItem(b.getName()));
			}
			_f.setLanguage();
		}
		catch (Exception e1) {e1.printStackTrace();}
	}

}
