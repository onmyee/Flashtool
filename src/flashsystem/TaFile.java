package flashsystem;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import org.logger.MyLogger;

/** Demonstrate using Scanner to read a file. **/
public class TaFile {
  
	FileInputStream _in;
	Scanner _scanner;
	Vector<TaEntry> entries = new Vector<TaEntry>();
	
	public TaFile(InputStream in) throws TaParseException {
		TaEntry entry;
		_in = (FileInputStream)in;
	    _scanner = new Scanner (in);
	    String partition="";
	    boolean beginentry=false;
	    while (_scanner.hasNextLine()) {
	    	String line = _scanner.nextLine().trim();
	    	if (!line.startsWith("/") && line.length()>=2) {
	    		Scanner scanline = new Scanner(line);
	    		scanline.useDelimiter(" ");
	    		while (scanline.hasNext()) {
	    			String elem = scanline.next();
	    			if (elem.length()==8) {
	    				if (partition.length()>0) {
	    					MyLogger.getLogger().debug("Closing entry "+partition);
	    					entries.get(entries.size()-1).close();
	    				}
	    				partition = elem;
	    				beginentry=true;
	    				entry = new TaEntry();
	    				entry.setPartition(partition);
	    				entries.add(entry);
	    			}
	    			else if (elem.length()==4 && beginentry) entries.get(entries.size()-1).setSize(elem);
	    			else if (elem.length()==2 && beginentry) entries.get(entries.size()-1).addData(elem);
	    			else beginentry=false;
	    		}
	    	}
	    }
			if (partition.length()>0) {
				MyLogger.getLogger().debug("Closing entry "+partition);
				entries.get(entries.size()-1).close();
			}
	    try {
	    	_in.close();
	    }
	    catch (Exception e) {}
	}

	public Vector<TaEntry> entries() {
		return entries;
	}
	
	public String toString() {
		String result = "";
		Iterator<TaEntry> i = entries().iterator();
		while (i.hasNext()) {
			TaEntry entry = i.next();
			result=result + entry+"\n"; 
		}
		return result;
	}
}