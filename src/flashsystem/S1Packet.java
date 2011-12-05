package flashsystem;

public class S1Packet {

	//[DWORD]  CMD
	//[DWORD]  FLAGS ( 1 | 2 | 4 )
	//[DWORD]  LEN
	//[BYTE]   HDR CHECKSUM
	//[BYTE[LEN]]  DATA
	//[DWORD]  DATA CHECKSUM (CRC32)
	byte[] cmd = new byte[4];
	byte[] flags = new byte[4];
	byte[] datalen = new byte[4];
	byte hdr;
	byte[] data;
	byte[] crc32 = new byte[4];
	int lastdatapos = 0;
	boolean finalized = false;
	
	public S1Packet(byte[] header) {
		if (header != null) {
			System.arraycopy(header, 0, cmd, 0, 4);
			System.arraycopy(header, 4, flags, 0, 4);
			System.arraycopy(header, 8, datalen, 0, 4);
			hdr = header[12];
			data = new byte[getDataLength()];
		}
		else finalized=true;
	}

	public byte[] getByteArray() {
		byte[] array = new byte[17+data.length];
		System.arraycopy(cmd, 0, array, 0, 4);
		System.arraycopy(flags, 0, array, 4, 4);
		System.arraycopy(datalen, 0, array, 8, 4);
		array[12] = hdr;
		System.arraycopy(data, 0, array, 13, data.length);
		System.arraycopy(crc32, 0, array, array.length-4, 4);
		return array;
	}

	public S1Packet(int pcommand, byte[] pdata, boolean ongoing) {
		cmd = BytesUtil.getBytesWord(pcommand, 4);
		flags = BytesUtil.getBytesWord(getFlag(false,true,ongoing), 4);
		if (pdata==null) data = new byte[0]; else data=pdata;
		datalen = BytesUtil.getBytesWord(data.length, 4);
		hdr = calculateHeaderCkSum();
		crc32=calculatedCRC32();
	}

	public S1Packet(int pcommand, byte pdata, boolean ongoing) {
		cmd = BytesUtil.getBytesWord(pcommand, 4);
		flags = BytesUtil.getBytesWord(getFlag(false,true,ongoing), 4);
		data = new byte[] {pdata};
		datalen = BytesUtil.getBytesWord(data.length, 4);
		hdr = calculateHeaderCkSum();
		crc32=calculatedCRC32();		
	}

	private int getFlag(boolean flag1, boolean flag2, boolean ongoing)
    {
        boolean flag = !flag1;
        byte byte0 = (byte)(flag2 ? 2 : 0);
        byte byte1 = (byte)(ongoing ? 4 : 0);
        return (((byte)(flag ? 1 : 0))) | byte0 | byte1;
    }

	public int getFlags() {
		return BytesUtil.getInt(flags);
	}
	
	public int getCommand() {
		return BytesUtil.getInt(cmd);
	}

	public int getDataLength() {
		return BytesUtil.getInt(datalen);
	}
	
	public byte[] getDataArray() {
		return data;
	}
	
	public void addData(byte[] datachunk) throws X10FlashException {
		if (lastdatapos<data.length) {
			System.arraycopy(datachunk, 0, data, lastdatapos, datachunk.length);
			lastdatapos+=datachunk.length;
		}
		else {
			System.arraycopy(datachunk, 0, crc32, 0, datachunk.length);
			finalized = true;
			if (BytesUtil.getLong(calculatedCRC32())!=BytesUtil.getLong(crc32))
				throw new X10FlashException("S1 Data CRC32 Error");
			if (calculateHeaderCkSum()!=hdr)
				throw new X10FlashException("S1 Header checksum Error");
		}
	}

	public String toString() {
		if (data == null) return "";
		return "Data : \t"+new String(data)+"\n"+"CRC32 : \t"+HexDump.toHex(crc32);
	}
	
	public byte[] calculatedCRC32() {
		if (data ==null) return null;
		return BytesUtil.getCRC32(data);
	}
	
	public boolean hasMoreToRead() {
		return !finalized;
	}

	public byte calculateHeaderCkSum()
    {
        byte header[] = new byte[12];
        System.arraycopy(cmd, 0, header, 0, 4);
        System.arraycopy(flags, 0, header, 4, 4);
        System.arraycopy(datalen, 0, header, 8, 4);
        return calcSum(header);
    }

	private byte calcSum(byte paramArray[])
    {
        byte byte0 = 0;
        if(paramArray.length < 12)
            return 0;
        for(int i = 0; i < 12; i++)
            byte0 ^= paramArray[i];

        byte0 += 7;
        return byte0;
    }

}
