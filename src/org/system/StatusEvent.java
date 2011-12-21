package org.system;

public class StatusEvent {
	String oldstatus;
	String newstatus;
	
	public StatusEvent(String olds, String news) {
		oldstatus=olds;
		newstatus = news;
	}
	
	public String getOld() {
		return oldstatus;
	}
	
	public String getNew() {
		return newstatus;
	}
	
	public boolean hasChanged() {
		return !newstatus.equals(oldstatus);
	}
}
