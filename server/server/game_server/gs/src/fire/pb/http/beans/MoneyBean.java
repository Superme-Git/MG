package fire.pb.http.beans;

public class MoneyBean {
	private byte type;
	private long value;
	
	public MoneyBean(byte type,long value){
		this.type=type;
		this.value=value;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
	

}
