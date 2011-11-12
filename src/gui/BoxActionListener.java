package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BoxActionListener implements ActionListener {
	
	AskBox _box;
	String _result;
	
	BoxActionListener(AskBox box,String result) {
		_box = box;
		_result = result;
	}
	
	public void actionPerformed(ActionEvent e) {
		_box.result = _result;
		_box.dispose();
	}
}
