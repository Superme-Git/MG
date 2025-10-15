package cn.com.zc.db.bean.mbean;

public class ConsumeRoleBean {

	private int consumeType;
	
	public int getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
	}

	private int roleNum;
	
	private int consumeTotal;
	
	public int getRoleNum() {
		return roleNum;
	}

	public void addRoleNum(int addNum) {
		roleNum += addNum;
	}

	public int getConsumeTotal() {
		return consumeTotal;
	}

	public void addConsumeTotal(int addConsumeTotal) {
		consumeTotal += addConsumeTotal;
	}
	
}
