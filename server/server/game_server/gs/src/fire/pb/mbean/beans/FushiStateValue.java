package fire.pb.mbean.beans;

public class FushiStateValue {
	
	private int userNum; //账号或角色总数
	
	private int totalAmount; //充值或者消耗总额
	
	public FushiStateValue(int userNum, int totalAmount){
		this.userNum = userNum;
		this.totalAmount = totalAmount;
	}
	
	public int getUserNum() {
		return userNum;
	}

	public int getTotalAmount() {
		return totalAmount;
	}


}
