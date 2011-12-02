package flashsystem;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.logger.MyLogger;

import flashsystem.io.USBFlash;

public class Command {

    private int lastflags;
    private boolean _simulate;
    byte lastReply[];

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
	
	public void testPlugged() throws X10FlashException {
    	if (!_simulate) {
	        readReply();
	        MyLogger.getLogger().debug("testPlugged");
	        MyLogger.getLogger().info(getLastReplyString());
    	}
	}
	
	public int getLastFlags() {
		return lastflags;
	}
	
	public void readReply() throws X10FlashException {
		lastReply = readDevReply();
		if (getLastReplyString().contains("ERR_SEVERITY"))
			if (!getLastReplyString().contains("ERR_SEVERITY=\"NONE\""))
				throw new X10FlashException(getLastReplyString());
	}

    private byte[] readDevReply() throws X10FlashException
    {
        try {
            byte abyte0[] = readBytes(0x10000);
            int i = -1;
            byte byte0 = -1;
            byte byte1 = -1;
            byte abyte1[] = abyte0;
            byte abyte2[] = new byte[4];
            System.arraycopy(abyte1, 0, abyte2, 0, 4);
            i = BytesUtil.getInt(abyte2);
            if((i & 0xfffffff0) == 0)
            {
                System.arraycopy(abyte1, 4, abyte2, 0, 4);
                int j = BytesUtil.getInt(abyte2);
                if((j & -8) == 0)
                {
                    System.arraycopy(abyte1, 8, abyte2, 0, 4);
                    int k = BytesUtil.getInt(abyte2);
                    if((k & 0xfffe0000) == 0)
                    {
                        byte byte2 = x10bytes.calcSum(abyte1);
                        if(byte2 == abyte1[12])
                        {
                            lastflags = j;
                            if(k != 0)
                            {
                                if(k >= 0x10000)
                                {
                                    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                                    DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
                                    do
                                    {
                                        if(k == 0)
                                            break;
                                        byte abyte5[] = readBytes(0x10000);
                                        try
                                        {
                                            dataoutputstream.write(abyte5);
                                        }
                                        catch(IOException ioexception)
                                        {
                                            break;
                                        }
                                        k -= abyte0.length;
                                    } while(true);
                                    abyte0 = bytearrayoutputstream.toByteArray();
                                } else
                                {
                                    abyte0 = readBytes(0x10000);
                                }
                            } else
                            {
                                byte abyte3[] = new byte[0];
                                abyte0 = abyte3;
                            }
                            byte abyte4[] = readBytes(4096);
                            int l = -1;
                            int i1 = -1;
                            if(abyte4.length == 4)
                                l = BytesUtil.getInt(abyte4);
                            x10bytes d1 = new x10bytes(i, false, false, false, abyte0);
                            byte abyte6[] = d1.getByteArray();
                            byte abyte7[] = new byte[4];
                            System.arraycopy(abyte6, abyte6.length - 4, abyte7, 0, 4);
                            i1 = BytesUtil.getInt(abyte7);
                            if(l != i1)
                                throw new X10FlashException(new StringBuilder("### read filed : checksum err! ").append(HexDump.toHex(l)).append("!=").append(HexDump.toHex(i1)).toString());
                            return abyte0;
                        }
                    }
                }
            }
        }
        catch (X10FlashException xe) {
        	throw xe;
        }
        catch(Exception exception) {}
        return null;
    }

    public String getLastReplyString() {
    	try {
    		return new String(lastReply);
    	}
    	catch (Exception e) {
    		return "";
    	}
    }

    public String getLastReplyHex() {
    	try {
    		return HexDump.toHex(lastReply);
    	}
    	catch (Exception e) {
    		return "";
    	}
    }

    public short getLastReplyLength() {
    	try {
    		return (short)lastReply.length;
    	}
    	catch (Exception e) {
    		return 0;
    	}
    }

    private void writeCommand(int command, byte abyte0[], boolean ongoing) throws X10FlashException {
    	try {
	    	if (!_simulate) {
	    		x10bytes d1 = new x10bytes(command, false, true, ongoing, abyte0);
	    		System.out.println(HexDump.toHex(d1.getByteArray()));
	    		if (!USBFlash.writeBytes(d1.getByteArray())) {
	    			throw new X10FlashException("Error writing command");
	    		}
	    		MyLogger.getLogger().debug((new StringBuilder("write(cmd=")).append(command).append(") (").append(ongoing? "continue)":"finish)").toString());
	    		
	    	}
	    	MyLogger.updateProgress();
    	}
    	catch (RuntimeException rte) {
    		throw new X10FlashException("Error writing command");
    	}
    	catch (Exception e) {
    		throw new X10FlashException("Error writing command");
    	}
    }

    private byte[] readBytes(int bufferlen) throws X10FlashException {
    	try {
	    	MyLogger.updateProgress();
	    	if (!_simulate) {
	    		byte[] ret = USBFlash.readBytes(bufferlen);
	    		return ret;
	    	}
	    	else return null;
    	}
		catch (RuntimeException rte) {
			throw new X10FlashException("Error reading reply");
		}
		catch (Exception e) {
			throw new X10FlashException("Error reading reply");
		}
    }

    public void send(int cmd, byte abyte0[], boolean ongoing) throws X10FlashException
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

    /*    private byte[] testCmd12(int i) throws IOException 
    {
    	if (!_bundle.simulate()) {
        byte abyte0[] = c.a(i, 4, false);
        writeCmd(12, abyte0,false);
        byte abyte1[] = readReply();
        if(abyte1 != null)
        {
            if(abyte1.length != 0)
            {
                MyLogger.getLogger().debug((new StringBuilder("<<<")).append(new String(abyte1)).toString());
                MyLogger.getLogger().debug((new StringBuilder("<<<")).append(HexDump.toHex(abyte1)).toString());
            } else
            {
                MyLogger.getLogger().debug("<<< null string");
            }
        } else
        {
        	MyLogger.getLogger().debug("<<< (null)");
        }
        return abyte1;
    	} else return null;
    }*/

}