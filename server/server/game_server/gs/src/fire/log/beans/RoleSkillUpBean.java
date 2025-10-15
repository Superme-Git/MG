package fire.log.beans;

public class RoleSkillUpBean {
	private int skillid;//技能ID
	private int level;//技能等级
	
	public RoleSkillUpBean(int skillid,int level){
		this.skillid=skillid;
		this.level=level;
	}

	public int getSkillid() {
		return skillid;
	}

	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	

}
