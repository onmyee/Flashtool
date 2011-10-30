package org.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import foxtrot.Job;
import foxtrot.Worker;

public class PluginActionListenerAbout implements ActionListener {

	PluginInterface _p;
	
	public PluginActionListenerAbout(PluginInterface p) {
		_p=p;
	}
	
	public void actionPerformed(ActionEvent e) {
		_p.showAbout();
	}

}
