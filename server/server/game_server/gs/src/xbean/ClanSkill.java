
package xbean;

public interface ClanSkill extends mkdb.Bean {
	public ClanSkill copy(); // deep clone
	public ClanSkill toData(); // a Data instance
	public ClanSkill toBean(); // a Bean instance
	public ClanSkill toDataIf(); // a Data instance If need. else return this
	public ClanSkill toBeanIf(); // a Bean instance If need. else return this

	public int getClanskillid(); // 技能id
	public int getClanskilllevel(); // 技能当前等级
	public int getClanskillexp(); // 技能当前经验

	public void setClanskillid(int _v_); // 技能id
	public void setClanskilllevel(int _v_); // 技能当前等级
	public void setClanskillexp(int _v_); // 技能当前经验
}
