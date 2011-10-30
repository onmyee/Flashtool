package org.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import foxtrot.Job;
import foxtrot.Worker;

public class PluginActionListener implements ActionListener {

	PluginInterface _p;
	
	public PluginActionListener(PluginInterface p) {
		_p=p;
	}
	
	public void actionPerformed(ActionEvent e) {
		Worker.post(new Job() {
			public Object run() {
				try {
					_p.run();
				}
				catch (Exception e) {}
				return null;
			}
		});
	}

}
