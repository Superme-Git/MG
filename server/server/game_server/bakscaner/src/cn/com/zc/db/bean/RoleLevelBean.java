package cn.com.zc.db.bean;

public class RoleLevelBean{
	
	private long roleid;
	
	private long exp;
	
	private int level;
	
	private int school;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static final int NUM_MAX = 100;
	
	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSchool() {
		return school;
	}

	public void setSchool(int school) {
		this.school = school;
	}
	
	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public boolean isGreater(RoleLevelBean other) {
		if(other.getLevel() > getLevel()){
			return false;
		}else if(other.getLevel() == getLevel()){
			if(other.getExp() > getExp()){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public int getNumMax() {
		
		return NUM_MAX;
	}


}
