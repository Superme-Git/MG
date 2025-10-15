package fire.pb.shop.utils;

public class InnerPetSkill implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1351791859296679271L;
	
	public int skillid;
	public int skillexp;
	public byte skilltype; // 0=先天技能  1=通过打书学习的技能
	public byte certification; // 0=未认证 1=认证
	
	public InnerPetSkill() {
		super();
	}
	public InnerPetSkill(int skillid, int skillexp, byte skilltype, byte certification) {
		super();
		this.skillid = skillid;
		this.skillexp = skillexp;
		this.skilltype = skilltype;
		this.certification = certification;
	}
	public int getSkillid() {
		return skillid;
	}
	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}
	public int getSkillexp() {
		return skillexp;
	}
	public void setSkillexp(int skillexp) {
		this.skillexp = skillexp;
	}
	public byte getSkilltype() {
		return skilltype;
	}
	public void setSkilltype(byte skilltype) {
		this.skilltype = skilltype;
	}
	public byte getCertification() {
		return certification;
	}
	public void setCertification(byte certification) {
		this.certification = certification;
	}
	
	
	
	
}
