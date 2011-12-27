package flashsystem;

import java.io.IOException;
import java.util.Arrays;

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

    private void writeCommand(int command, byte data[], boolean ongoing) throws X10FlashException, IOException {
	    	if (!_simulate) {
	    		S1Packet p = new S1Packet(command,data,ongoing);
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

    public void send(int cmd, byte data[], boolean ongoing) throws X10FlashException, IOException
    {
    	int maxdatalen=65536-17;
		int totallen = data.length;
    	int nbwrite = totallen/maxdatalen;
    	int remain = totallen%maxdatalen;
    	for (int i=0;i<nbwrite;i++) {
    		int begin = i*maxdatalen;
    		int end = (i+1)*maxdatalen;
    		writeCommand(cmd, Arrays.copyOfRange(data, begin, end), true);
    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());
    		if (USBFlash.getLastFlags()==0) {
    			writeCommand(Command.CMD07, Command.VALNULL, false);
    			throw new X10FlashException(getLastReplyString());
    		}
    	}
    	if (remain>0 || data.length==0) {
    		int begin = totallen-remain;
    		int end = totallen;
    		writeCommand(cmd, Arrays.copyOfRange(data, begin, end), ongoing);
    		MyLogger.getLogger().debug("Reply      : "+getLastReplyString());
    		MyLogger.getLogger().debug("Reply(Hex) : "+getLastReplyHex());	
    		if (USBFlash.getLastFlags()==0) {
    			writeCommand(Command.CMD07, Command.VALNULL, false);
    			throw new X10FlashException(getLastReplyString());
    		}
    	}
    }

}