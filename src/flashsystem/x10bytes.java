package flashsystem;

import java.io.*;

public class x10bytes
{

    public x10bytes(int pcmd, boolean flag, boolean flag1, boolean flag2, byte abyte0[])
    {
        cmd = pcmd;
        c = flag;
        d = flag1;
        e = flag2;
        body = abyte0 == null ? new byte[0] : abyte0;
        g = -1L;
    }

    public byte[] getByteArray()
    {
        long l = e();
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        try
        {
            dataoutputstream.write(createHeader());
            dataoutputstream.write(body);
            dataoutputstream.write(BytesUtil.getBytesWord(l, 4));
        }
        catch(IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        return bytearrayoutputstream.toByteArray();
    }

    private long e()
    {
        if(g == -1L)
        {
            Conv c1 = new Conv();
            c1.a(body);
            g = c1.b();
        }
        return g;
    }

    private int g()
    {
        boolean flag = !c;
        byte byte0 = (byte)(d ? 2 : 0);
        byte byte1 = (byte)(e ? 4 : 0);
        return (((byte)(flag ? 1 : 0))) | byte0 | byte1;
    }

    public static byte calcSum(byte hdr[])
    {
        byte byte0 = 0;
        if(hdr.length < 12)
            return 0;
        for(int i = 0; i < 12; i++)
            byte0 ^= hdr[i];

        byte0 += 7;
        return byte0;
    }

    private byte[] createHeader()
    {
        byte hdr[] = new byte[13];
        System.arraycopy(BytesUtil.getBytesWord(cmd, 4), 0, hdr, 0, 4);
        System.arraycopy(BytesUtil.getBytesWord(g(), 4), 0, hdr, 4, 4);
        System.arraycopy(BytesUtil.getBytesWord(body.length, 4), 0, hdr, 8, 4);
        hdr[12] = calcSum(hdr);
        return hdr;
    }

    private int cmd;
    private boolean c;
    private boolean d;
    private boolean e;
    private byte body[];
    private long g;
}
