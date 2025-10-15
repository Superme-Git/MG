package fire.log.beans;

public class SkillInfoBean {
	private int skillid;//
	private String changereason;//变化原因
	
	public SkillInfoBean(int skillid,String changereason){
		this.skillid=skillid;
		this.changereason=changereason;
	}

	public int getSkillid() {
		return skillid;
	}

	public String getChangereason() {
		return changereason;
	}
}
