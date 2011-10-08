package org.system;

import java.util.HashSet;
import java.util.Iterator;

public class RunStack {

	static HashSet<OsRun> set = new HashSet<OsRun>();
	
	public static void addToStack(OsRun p) {
		set.add(p);
	}
	
	public static void removeFromStack(OsRun p) {
		set.remove(p);
	}
	
	public static void killAll() {
		Iterator<OsRun> i = set.iterator();
		while (i.hasNext())
			i.next().destroy();
	}
}
