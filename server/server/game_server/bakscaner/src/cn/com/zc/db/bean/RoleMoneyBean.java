package cn.com.zc.db.bean;

public class RoleMoneyBean{

	private static final int NUM_MAX = 100;
	
	private long roleid;
	
	private long money;
	
	private int school;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGreater(RoleMoneyBean other) {

		if(other.getMoney() > getMoney()){
			return false;
		}
		
		return true;
	}
	
	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public int getSchool() {
		return school;
	}

	public void setSchool(int school) {
		this.school = school;
	}

	public int getNumMax() {
		
		return NUM_MAX;
	}

}
