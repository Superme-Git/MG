package cn.com.zc.db.bean.mbean;

public class ChargeUserBean {

	private int userNum;
	
	private int chargeTotal;
	
	public int getUserNum() {
		return userNum;
	}

	public void addUserNum(int addNum) {
		userNum += addNum;
	}

	public int getChargeTotal() {
		return chargeTotal;
	}

	public void addChargeTotal(int addChargeTotal) {
		chargeTotal += addChargeTotal;
	}

}
