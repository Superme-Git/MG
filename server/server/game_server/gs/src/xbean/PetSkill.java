
package xbean;

public interface PetSkill extends mkdb.Bean {
	public PetSkill copy(); // deep clone
	public PetSkill toData(); // a Data instance
	public PetSkill toBean(); // a Bean instance
	public PetSkill toDataIf(); // a Data instance If need. else return this
	public PetSkill toBeanIf(); // a Bean instance If need. else return this

	public int getSkillid(); // 技能id
	public int getSkilltype(); // 0=先天技能  1=后天通过技能书打上去的技能
	public int getCertification(); // 0=未认证 1=认证

	public void setSkillid(int _v_); // 技能id
	public void setSkilltype(int _v_); // 0=先天技能  1=后天通过技能书打上去的技能
	public void setCertification(int _v_); // 0=未认证 1=认证
}
