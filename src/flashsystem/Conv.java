package flashsystem;

public class Conv
{
    private long a;
    private static long b[];

    public Conv()
    {
        a = 0L;
    }

    private void a(byte byte0)
    {
        long l = 255L & (long)byte0 & 0xffffffffL;
        long l1 = (a ^ l) & 0xffffffffL;
        a = (a >> 8 & 0xffffffffL ^ b[(int)(l1 & 255L)]) & 0xffffffffL;
    }

    public void a(byte abyte0[])
    {
        for(int i = 0; i < abyte0.length; i++)
            a(abyte0[i]);

    }

    public long b()
    {
        return a & 0xffffffffL;
    }

    static 
    {
        b = new long[256];
        for(int i = 0; i < 256; i++)
        {
            long l1 = i;
            for(int j = 0; j < 8; j++)
            {
                if(l1 % 2L == 0L)
                    l1 = l1 >> 1 & 0xffffffffL;
                else
                    l1 = (l1 >> 1 ^ 0xedb88320L) & 0xffffffffL;
                b[i] = l1;
            }

        }

    }
}
