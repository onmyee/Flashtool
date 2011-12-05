package flashsystem;

import java.math.BigInteger;
import java.util.Arrays;

public class BytesUtil {

	private static long pivot[];
	static final String HEXES = "0123456789ABCDEF";

	
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

	public static long getLong(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null)
        return 0;
      byte[] arrayOfByte = paramArrayOfByte;
      int i = arrayOfByte.length;
      if (i < 4)
        arrayOfByte = bytesconcat(new byte[4 - i], arrayOfByte);
      return new BigInteger(arrayOfByte).longValue();
    }

	public static byte[] getCRC32(byte[] paramArrayOfByte) {
		long result = 0L;
		for(int i = 0; i < paramArrayOfByte.length; i++) {
	        long l = 255L & (long)paramArrayOfByte[i] & 0xffffffffL;
	        long l1 = (result ^ l) & 0xffffffffL;
	        result = (result >> 8 & 0xffffffffL ^ pivot[(int)(l1 & 255L)]) & 0xffffffffL;
		}
		return getBytesWord(result & 0xffffffffL,4);
	}

	public static String getHex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	 }

	public static Byte[] getBytes(String hexString) {
	    int len = hexString.length();
	    Byte[] data = new Byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
	                             + Character.digit(hexString.charAt(i+1), 16));
	    }
	    return data;
	}

	public static <T> T[] concatAll(T[] first, T[]... rest) {
		  int totalLength = first.length;
		  for (T[] array : rest) {
		    totalLength += array.length;
		  }
		  T[] result = Arrays.copyOf(first, totalLength);
		  int offset = first.length;
		  for (T[] array : rest) {
		    System.arraycopy(array, 0, result, offset, array.length);
		    offset += array.length;
		  }
		  return result;
	}

	
	
	static 
    {
        pivot = new long[256];
        for(int i = 0; i < 256; i++)
        {
            long l1 = i;
            for(int j = 0; j < 8; j++)
            {
                if(l1 % 2L == 0L)
                    l1 = l1 >> 1 & 0xffffffffL;
                else
                    l1 = (l1 >> 1 ^ 0xedb88320L) & 0xffffffffL;
                pivot[i] = l1;
            }

        }

    }


}
