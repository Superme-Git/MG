
package xbean;

public interface SpecialSkill extends mkdb.Bean {
	public SpecialSkill copy(); // deep clone
	public SpecialSkill toData(); // a Data instance
	public SpecialSkill toBean(); // a Bean instance
	public SpecialSkill toDataIf(); // a Data instance If need. else return this
	public SpecialSkill toBeanIf(); // a Bean instance If need. else return this

	public int getSkillid(); // 
	public int getEquiptype(); // 

	public void setSkillid(int _v_); // 
	public void setEquiptype(int _v_); // 
}
