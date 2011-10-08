package gui;

import java.awt.Color;
import java.io.File;

public class ListItem {
	
	private Color _foreground;
	private Color _background;
    private String _realname;
    private String _apkname;
    private boolean isSaved;

    public ListItem(String apkname, String realname, Color foreground, Color background) {
    	_apkname = apkname;
    	_realname = realname;
        _foreground = foreground;
        _background = background;
        isSaved = (new File("./custom/apps_saved/"+apkname).isFile());
    }
    
    public Color getForeground() {
        return _foreground;
    }

    public Color getBackground() {
        return _background;
    }

    public String getName() {
        return _realname;
    }

    public String getFile() {
    	return _apkname;
    }
    
    public boolean fileExists() {
    	return isSaved;
    }
    
    public void setForeground(Color c) {
    	_foreground=c;
    }

    public void setBackground(Color c) {
    	_background=c;
    }

    public String toString() {
    	return _realname;
    }
}
