package org.regtool;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Set;
import java.util.HashSet;

import org.logger.MyLogger;
import org.system.OsRun;

/**
 * @author Oleg Ryaboy, based on work by Miguel Enriquez 
 */
public class Registry {

    /**
     * 
     * @param location path in the registry
     * @param key registry key
     * @return registry value or null if not found
     */
    public static final Set<String > listNodes(String noderoot){
    	noderoot=noderoot.toUpperCase();
    	Set<String> s = new HashSet();
        try {
        	MyLogger.debug("Running "+"reg query " + '"'+ noderoot + '"');
        	OsRun run = new OsRun("reg query " + '"'+ noderoot + '"');
        	run.run();
            String[] parsed = run.getStdOut().split("\n");
            for (int i=0;i<parsed.length;i++) {
            	parsed[i]=parsed[i].toUpperCase();
            	if (parsed[i].contains(noderoot)) {
            		String[] nodes = parsed[i].split("ADDE");
            		if (nodes.length>1) {
            			MyLogger.debug("Adding "+nodes[1].toLowerCase().substring(1));
            			s.add(nodes[1].toLowerCase().substring(1));
            		}
            	}
            }
        }
        catch (Exception e) {}
        return s;
    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();;

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) { 
        }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}