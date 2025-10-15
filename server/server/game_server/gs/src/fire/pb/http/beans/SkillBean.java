package fire.pb.http.beans;

public class SkillBean {
	private int skillid;
	private int skilllevel;
	
	public SkillBean(int skillid,int skilllevel){
		this.skillid=skillid;
		this.skilllevel=skilllevel;
	}
	
	public int getSkillid() {
		return skillid;
	}
	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}
	public int getSkilllevel() {
		return skilllevel;
	}
	public void setSkilllevel(int skilllevel) {
		this.skilllevel = skilllevel;
	}
	
	

}
