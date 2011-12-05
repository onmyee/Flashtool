package flashsystem;

public class TaEntry {

	String _partition="";
	String _size="";
	String _data="";
	
	public TaEntry() {
	}
	
	public void setPartition(String partition) {
		_partition = partition;
	}
	
	public void setSize(String size) {
		_size = "0000"+size;
	}
	
	public void addData(String data) {
		_data = _data + " "+data;
		_data = _data.trim();
		
	}
	
	public String getPartition() {
		return _partition;
	}
	
	public Byte[] getPartitionBytes() {
		return BytesUtil.getBytes(_partition);
	}

	public String getSize() {
		return _size;
	}

	public Byte[] getSizeBytes() {
		return BytesUtil.getBytes(_size);
	}
	
	public String getData() {
		return _data;
	}
	
	public Byte[] getDataBytes() {
		String[] datas = _data.split(" ");
		Byte[] data = new Byte[datas.length];
		for (int j=0;j<datas.length;j++) {
			data[j]=BytesUtil.getBytes(datas[j])[0];
		}
		return data;
	}
	
	public Byte[] getWordByte() {
		return BytesUtil.concatAll(getPartitionBytes(), getSizeBytes(), getDataBytes());
	}
	
	public byte[] getWordbyte() {
		Byte[] b1 = getWordByte();
		byte[] b = new byte[b1.length];
		for (int i=0;i<b1.length;i++) {
			b[i]=b1[i];
		}
		return b;
	}

	public String toString() {
		return getPartition()+" "+getSize()+" "+getData();
	}
	
	public void close() throws TaParseException {
		if (Integer.parseInt(getSize(),16)!=getDataBytes().length) {
			throw new TaParseException("TA entry ("+getPartition()+")parsing error");
		}
	}
}
