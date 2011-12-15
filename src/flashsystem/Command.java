package flashsystem;

import java.io.IOException;

import org.logger.MyLogger;

import flashsystem.io.USBFlash;

public class Command {

    private boolean _simulate;

	static final byte[] TA_FLASH_STARTUP_SHUTDOWN_RESULT_ONGOING	     = { 
		   0, 0, 8, -77, 0, 0, 0, 4, -96, 0, 0, 0 }; 
	static final byte[] TA_EDREAM_FLASH_STARTUP_SHUTDOWN_RESULT_ONGOING	 = {
		   0, 0, 39, 116, 0, 0, 0, 1, 1 };
	static final byte[] TA_FLASH_STARTUP_SHUTDOWN_RESULT_FINISHED		 = {
		   0, 0, 8, -77, 0, 0, 0, 4, -86, 0, 0, 0 };
	static final byte[] TA_EDREAM_FLASH_STARTUP_SHUTDOWN_RESULT_FINISHED = {
		   0, 0, 39, 116, 0, 0, 0, 1, 0 };
	
	static final int CMD01 = 1;
	static final int CMD04 = 4;
	static final int CMD05 = 5;
	static final int CMD06 = 6;
	static final int CMD07 = 7;
	static final int CMD09 = 9;
	static final int CMD10 = 10;
	static final int CMD12 = 12;
	static final int CMD13 = 13;
	
	static final byte[] VALNULL = new byte[0];
	static final byte[] VAL1 = new byte[] {1};
	static final byte[] VAL2 = new byte[] {2};

	public Command(boolean simulate) {
		_simulate = simulate;
	}
	
	public void readReply() throws X10FlashException,IOException {
		if (getLastReplyString().contains("ERR_SEVERITY"))
			if (!getLastReplyString().contains("ERR_SEVERITY=\"NONE\""))
				throw new X10FlashException(getLastReplyString());
	}

    public String getLastReplyString() {
    	try {
    		return new String(USBFlash.getLastReply());
    	}
    	catch (Exception e) {
    		return "";
    	}
    }

    public String getLastReplyHex() {
    	try {
    		return HexDump.toHex(USBFlash.getLastReply());
    	}
    	catch (Exception e) {
    		return "";
    	}
    }

    public short getLastReplyLength() {
    	try {
    		return (short)USBFlash.getLastReply().length;
    	}
    	catch (Exception e) {
    		return 0;
    	}
    }

    private void writeCommand(int command, byte abyte0[], boolean ongoing) throws X10FlashException, IOException {
	    	if (!_simulate) {
	    		S1Packet p = new S1Packet(command,abyte0,ongoing);
	    		try {
	    			USBFlash.write(p);
	    			p.release();
	    		}
	    		catch (X10FlashException xe) {
	    			p.release();
	    			throw new X10FlashException(xe.getMessage());
	    		}
	    		catch (IOException ioe) {
	    			p.release();
	    			throw new IOException(ioe.getMessage());
	    		}
	    	}
	    	MyLogger.updateProgress();
    }

    public void send(int cmd, byte abyte0[], boolean ongoing) throws X10FlashException, IOException
    {
    	if (cmd==Command.CMD06) {
	        if(abyte0 != null && abyte0.length < 65519) {
	            writeCommand(Command.CMD06, abyte0, ongoing);
	    		readReply();
	    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
	    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());
	        } 
	        else {
	            byte abyte1[] = new byte[65519];
	            System.arraycopy(abyte0, 0, abyte1, 0, abyte1.length);
	            writeCommand(Command.CMD06, abyte1, true);
	    		readReply();
	    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
	    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());
	    		abyte1 = new byte[abyte0.length - 65519];
	            System.arraycopy(abyte0, 65519, abyte1, 0, abyte1.length);
	            writeCommand(Command.CMD06, abyte1, ongoing);
	    		readReply();
	    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
	    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());
	        }
    	}
    	else {
    		writeCommand(cmd, abyte0, ongoing);
    		readReply();
    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());	
    	}
    }

}