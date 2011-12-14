package org.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.logger.MyLogger;


public class PluginActionListener implements ActionListener {

	PluginInterface _p;
	
	public PluginActionListener(PluginInterface p) {
		_p=p;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			_p.run();
		}
		catch (Exception ex) {
			MyLogger.getLogger().error(ex.getMessage());
		}
	}

}
