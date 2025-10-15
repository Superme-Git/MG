package fire.pb.mbean.beans;

public class ChargeUserKey {

	private int chargeType; //付费类型
	
	private int chargeValue; //付费区间段

	public ChargeUserKey(int chargeType, int chargeValue){
		this.chargeType = chargeType;
		this.chargeValue = chargeValue;
	}
	public int getChargeType() {
		return chargeType;
	}

	public int getChargeValue() {
		return chargeValue;
	}

	@Override
	public int hashCode(){
		return chargeType ^ chargeValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		ChargeUserKey other = (ChargeUserKey)obj;
		return (other.chargeType == this.chargeType) && (other.chargeValue == this.chargeValue);
	}
	
}
