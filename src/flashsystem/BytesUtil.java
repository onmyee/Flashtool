package flashsystem;

import java.math.BigInteger;

public class BytesUtil {

	public static byte[] getBytesWord(int paramInt1, int paramInt2)
    {
//      int m = 256;
      int i = paramInt2;
      if ((paramInt2 < 1) || (paramInt2 > 4))
        i = 4;
      byte[] arrayOfByte = new byte[i];
      int j = 0;
      int k;
        k = i - 1;
        do
        {
          arrayOfByte[k] = (byte)(paramInt1 >> j & 0xFF);
          j += 8;
          k--;
        }
        while (k >= 0);
      return arrayOfByte;
    }

	public static byte[] getBytesWord(long paramLong, int paramInt)
    {
      //int m = 256;
      int i = paramInt;
      if ((paramInt < 1) || (paramInt > 8))
        i = 8;
      byte[] arrayOfByte1 = new byte[8];
      arrayOfByte1[0] = (byte)(int)(paramLong >>> 56);
      arrayOfByte1[1] = (byte)(int)(paramLong >>> 48);
      arrayOfByte1[2] = (byte)(int)(paramLong >>> 40);
      arrayOfByte1[3] = (byte)(int)(paramLong >>> 32);
      arrayOfByte1[4] = (byte)(int)(paramLong >>> 24);
      arrayOfByte1[5] = (byte)(int)(paramLong >>> 16);
      arrayOfByte1[6] = (byte)(int)(paramLong >>> 8);
      arrayOfByte1[7] = (byte)(int)(paramLong >>> 0);
      byte[] localObject = new byte[i];
      int j = 8 - i;
      int k = 0;
        do
        {
          localObject[k] = arrayOfByte1[j];
          j++;
          k++;
        }
        while (k < i);
      return localObject;
    }

	public static byte[] bytesconcat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      byte[] arrayOfByte = (byte[])null;
      if ((paramArrayOfByte1 == null) && (paramArrayOfByte2 != null))
      {
        arrayOfByte = new byte[paramArrayOfByte2.length];
        System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, paramArrayOfByte2.length);
      }
      else if ((paramArrayOfByte1 != null) && (paramArrayOfByte2 == null))
      {
        arrayOfByte = new byte[paramArrayOfByte1.length];
        System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
      }
      else if ((paramArrayOfByte1 != null) && (paramArrayOfByte2 != null))
      {
        arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
        System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
        System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
      }
      return arrayOfByte;
    }

	public static int getInt(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null)
        return 0;
      byte[] arrayOfByte = paramArrayOfByte;
      int i = arrayOfByte.length;
      if (i < 4)
        arrayOfByte = bytesconcat(new byte[4 - i], arrayOfByte);
      return new BigInteger(arrayOfByte).intValue();
    }

}
