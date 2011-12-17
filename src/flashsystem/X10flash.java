package flashsystem;

import flashsystem.HexDump;
import flashsystem.io.USBFlash;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import org.logger.MyLogger;
import org.system.Device;
import org.system.DeviceChangedListener;
import org.system.TextFile;

import java.util.Enumeration;
import java.util.Vector;

public class X10flash {

    private Bundle _bundle;
    private Command cmd;
    private String phoneident = "";
    private String loaderident = "";

    public X10flash(Bundle bundle) {
    	_bundle=bundle;
    }

    private void setFlashState(boolean ongoing) throws IOException,X10FlashException
    {
	    	if (ongoing) {
	    		cmd.send(Command.CMD13,Command.TA_FLASH_STARTUP_SHUTDOWN_RESULT_ONGOING,false);
	    		cmd.send(Command.CMD13, Command.TA_EDREAM_FLASH_STARTUP_SHUTDOWN_RESULT_ONGOING,false);    		
	    	}
	    	else {
	    		cmd.send(Command.CMD13, Command.TA_FLASH_STARTUP_SHUTDOWN_RESULT_FINISHED,false);
	        	cmd.send(Command.CMD13, Command.TA_EDREAM_FLASH_STARTUP_SHUTDOWN_RESULT_FINISHED,false);
	    	}
    }

    private void sendTA(InputStream in,String name) throws FileNotFoundException, IOException,X10FlashException {
    	try {
    		TaFile ta = new TaFile(in);
    		MyLogger.getLogger().info("Flashing "+name);
			Vector<TaEntry> entries = ta.entries();
			for (int i=0;i<entries.size();i++) {
				MyLogger.getLogger().info("TA value : "+HexDump.toHex(entries.get(i).getWordbyte()));
				if (!_bundle.simulate()) {
					cmd.send(Command.CMD13, entries.get(i).getWordbyte(),false);
				}
			}
    	}
    	catch (TaParseException tae) {
    		MyLogger.getLogger().error("Error parsing TA file. Skipping");
    	}
    }

    public String dumpProperty(int prnumber) throws IOException, X10FlashException
    {
    	try {
    		MyLogger.initProgress(1);
    		MyLogger.getLogger().info("Start Reading property");
	        MyLogger.getLogger().debug((new StringBuilder("%%% read property id=")).append(prnumber).toString());
	        cmd.send(Command.CMD12, BytesUtil.getBytesWord(prnumber, 4),false);
	        String reply = cmd.getLastReplyHex();
	        reply = reply.replace("[", "");
	        reply = reply.replace("]", "");
	        reply = reply.replace(",", "");
			MyLogger.getLogger().info("Reading property finished.");
			MyLogger.initProgress(0);
			DeviceChangedListener.pause(false);
			return reply;
	    }
    	catch (Exception ioe) {
    		MyLogger.getLogger().error("Error dumping properties. Aborted");
    		MyLogger.initProgress(0);
    		DeviceChangedListener.pause(false);
    		return "";
    	}    	
    }

    public void dumpProperties() throws IOException, X10FlashException
    {
        TextFile tazone = new TextFile("./tazone.ta","ISO8859-1");
        tazone.open(false);
        TextFile tazoneS = new TextFile("./tazoneString.ta","ISO8859-1");
        tazoneS.open(false);
    	try {
		    MyLogger.getLogger().info("Start Dumping properties");
		    MyLogger.initProgress(3000);
	        for(int i = 0; i < 3000; i++)
	        {
	        	MyLogger.getLogger().debug((new StringBuilder("%%% read property id=")).append(i).toString());
	        	cmd.send(Command.CMD12, BytesUtil.getBytesWord(i, 4),false);
	        	String reply = cmd.getLastReplyHex();
	        	String replyS = cmd.getLastReplyString();
	        	reply = reply.replace("[", "");
	        	reply = reply.replace("]", "");
	        	reply = reply.replace(",", "");
	        	if (cmd.getLastReplyLength()>0) {
	        		tazone.writeln(HexDump.toHex(i) + " " + HexDump.toHex(cmd.getLastReplyLength()) + " " + reply.trim());
	        		tazoneS.writeln(HexDump.toHex(i) + " " + HexDump.toHex(cmd.getLastReplyLength()) + " " + replyS.trim());
	        	}
	        }
	        MyLogger.initProgress(0);
	        tazone.close();
	        tazoneS.close();
			MyLogger.getLogger().info("Dumping properties finished.");
			DeviceChangedListener.pause(false);
	    }
    	catch (Exception ioe) {
	        tazone.close();
	        tazoneS.close();
    		MyLogger.initProgress(0);
    		MyLogger.getLogger().error(ioe.getMessage());
    		MyLogger.getLogger().error("Error dumping properties. Aborted");
    		DeviceChangedListener.pause(false);
    	}
    }

    private void processHeader(InputStream fileinputstream) throws X10FlashException {
    	byte abyte2[];
    	try {
			byte abyte0[] = new byte[6];
			int j = fileinputstream.read(abyte0);
			if(j != 6) {
				fileinputstream.close();
				throw new X10FlashException("Error in processHeader");
			}
			int k;
			byte abyte1[] = new byte[4];
			System.arraycopy(abyte0, 2, abyte1, 0, 4);
			k = BytesUtil.getInt(abyte1);
			abyte1 = new byte[k - 6];
			k = fileinputstream.read(abyte1);
			if(k != abyte1.length) {
				fileinputstream.close();
				throw new X10FlashException("Error in processHeader");
			}
			abyte2 = new byte[k + 6];
			System.arraycopy(abyte0, 0, abyte2, 0, 6);
			System.arraycopy(abyte1, 0, abyte2, 6, abyte2.length - 6);
            cmd.send(Command.CMD05,abyte2,false);
            if (USBFlash.getLastFlags() == 0)
                cmd.send(Command.CMD07,Command.VALNULL,false);
    	}
    	catch (IOException ioe) {
    		throw new X10FlashException("Error in processHeader : "+ioe.getMessage());
    	}
    }
 
    private void uploadImage(InputStream fileinputstream, int buffer) throws X10FlashException {
    	try {
	    	processHeader(fileinputstream);
	    	byte readarray[] = new byte[buffer];
			int readCount;
			do {
				readCount = fileinputstream.read(readarray);
				if(readCount != buffer)
					break;
				cmd.send(Command.CMD06, readarray, true);
			} while(true);
			fileinputstream.close();
	        if (readCount != 0) {
	            byte abyte4[] = new byte[readCount];
	            System.arraycopy(readarray, 0, abyte4, 0, readCount);
	            cmd.send(Command.CMD06, abyte4, false);
	        }
    	}
    	catch (Exception e) {
    		try {fileinputstream.close();}catch(Exception cl) {}
    		throw new X10FlashException (e.getMessage());
    	}
    }

    public void sendLoader() throws FileNotFoundException, IOException, X10FlashException {
		MyLogger.getLogger().info("Flashing loader");
		uploadImage(_bundle.getLoader().getInputStream(), 0x1000);
    }

    public void sendSystemAndUserData() throws FileNotFoundException,IOException, X10FlashException {
        Enumeration<BundleEntry> e = _bundle.systemdataEntries();
    	while (e.hasMoreElements()) {
    		BundleEntry entry = e.nextElement();
    		MyLogger.getLogger().info("Flashing "+entry.getName());
    		uploadImage(entry.getInputStream(),0x10000);
    	}    	
    }
    
    public void sendImages() throws FileNotFoundException, IOException,X10FlashException {            
    		Enumeration<BundleEntry> e = _bundle.entries();
        	while (e.hasMoreElements()) {
        		BundleEntry entry = e.nextElement();
        		MyLogger.getLogger().info("Flashing "+entry.getName());
        		uploadImage(entry.getInputStream(),0x10000);
        		MyLogger.getLogger().debug("Flashing "+entry.getName()+" finished");
        	}
    }

    public long getNumberPasses() {
	    Enumeration<BundleEntry> e = _bundle.allEntries();
	    long totalsize = 0;
	    while (e.hasMoreElements()) {
	    	BundleEntry entry = e.nextElement();
	    	if (entry.getName().contains("loader"))
	    		totalsize = totalsize + entry.getSize()/0x1000+1;
	    	else 
	    		totalsize = totalsize + (entry.getSize()/0x10000)*2+3;
	    }
	    return totalsize+13;
    }

    public String getPhoneIdent() {
    	return phoneident;
    }

    public String getLoaderIdent() {
    	return loaderident;
    }

    private void init() throws X10FlashException,FileNotFoundException, IOException {
		cmd.send(Command.CMD09, Command.VAL2, false);
        cmd.send(Command.CMD10, Command.VALNULL, false);
		sendLoader();		
		cmd.send(Command.CMD01, Command.VALNULL, false);
		loaderident = cmd.getLastReplyString();
		MyLogger.getLogger().info(loaderident);
        cmd.send(Command.CMD09, Command.VAL2,false);    	
    }

    public void flashDevice() {
    	try {
		    MyLogger.getLogger().info("Start Flashing");
		    MyLogger.initProgress(getNumberPasses());
		    
		    init();
		    
			setFlashState(true);
			
			sendImages();
        	if (_bundle.hasPreset()) sendTA(_bundle.getPreset().getInputStream(),"preset");
        	//if (_bundle.hasSimlock()) sendTA(_bundle.getSimlock().getInputStream(),"simlock");
        	sendSystemAndUserData();

			cmd.send(Command.CMD07,Command.VALNULL,false);
			
			setFlashState(false);
            
			cmd.send(Command.CMD07,Command.VALNULL,false);
            
			cmd.send(Command.CMD10,Command.VALNULL,false);
            
			cmd.send(Command.CMD04,Command.VAL1,false);
	
			MyLogger.getLogger().info("Flashing finished.");
		    MyLogger.initProgress(0);
		    DeviceChangedListener.pause(false);
    	}
    	catch (Exception ioe) {
    		MyLogger.getLogger().error(ioe.getMessage());
    		MyLogger.getLogger().error("Error flashing. Aborted");
    		MyLogger.initProgress(0);
    		DeviceChangedListener.pause(false);
    	}
    }

    public boolean openDevice() {
    	return openDevice(_bundle.simulate());
    }

    public boolean deviceFound() {
    	boolean found = false;
    	try {
			Thread.sleep(500);
			found = Device.getLastConnected().getPid().equals("ADDE");
		}
		catch (Exception e) {
	    	found = false;
		}
    	return found;
    }

    public boolean openDevice(boolean simulate) {
    	if (simulate) return true;
    	boolean found=false;
    	try {
    		USBFlash.open();
    		phoneident = new String (USBFlash.getLastReply());
    	    cmd = new Command(_bundle.simulate());	
    		cmd.send(Command.CMD01, Command.VALNULL, false);
    		loaderident = new String (USBFlash.getLastReply());
    		found = true;
    	}
    	catch (Exception e){
    		found=false;    		
    	}
    	return found;
    }

}