package flashsystem.io;

import com.sonyericsson.cs.usbflashnative.impl.USBFlashNativeInterface;
import com.sonyericsson.cs.usbflashnative.impl.USBFlashNativeImpl;

import flashsystem.BytesUtil;
import flashsystem.HexDump;
import flashsystem.X10FlashException;
import flashsystem.x10bytes;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.system.OS;

public class USBFlash {
	
	private static USBFlashNativeInterface usbflash = new USBFlashNativeImpl();
	private static int channel=-1;
	private static int lastflags;
	
	public static int openChannel(String paramString) throws IOException {
		channel = usbflash.openChannel(paramString, false);
		try {
			System.out.println(HexDump.toHex(readDevReply()));
		}catch (Exception e) {
		}
		return channel;
	}
	
	public static boolean close() throws IOException {
		boolean result = usbflash.close(channel);
		channel = -1;
		return result;
	}

	public static boolean writeBytes(byte[] paramArrayOfByte) throws IOException {
		return usbflash.writeBytes(channel, paramArrayOfByte);
	}

	public static byte[] readBytes(int paramInt2) throws IOException {
		return usbflash.readBytes(channel, paramInt2);
	}

    public static  byte[] readDevReply() throws X10FlashException
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

	static {
		if (OS.getName().equals("windows")) System.loadLibrary("USBFlash");
	}

}