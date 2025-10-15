
package xbean;

public interface HuoBanshuxing extends mkdb.Bean {
	public HuoBanshuxing copy(); // deep clone
	public HuoBanshuxing toData(); // a Data instance
	public HuoBanshuxing toBean(); // a Bean instance
	public HuoBanshuxing toDataIf(); // a Data instance If need. else return this
	public HuoBanshuxing toBeanIf(); // a Bean instance If need. else return this

	public String getHuobanname(); // 名字
	public com.locojoy.base.Octets getHuobannameOctets(); // 名字
	public int getLevel(); // 等级
	public int getSchool(); // 职业
	public int getShape(); // 造型Id
	public float getBodytype(); // 大小
	public java.util.Map<Integer, Float> getEffects(); // key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // key = effect type id
	public java.util.Map<Integer, Float> getFinalattrs(); // key = attr type
	public java.util.Map<Integer, Float> getFinalattrsAsData(); // key = attr type
	public java.util.List<Integer> getSkills(); // huoban拥有的战斗技能
	public java.util.List<Integer> getSkillsAsData(); // huoban拥有的战斗技能
	public int getSkillmaster(); // 精通技能
	public java.util.List<Integer> getBattleai(); // huoban拥有的战斗ai
	public java.util.List<Integer> getBattleaiAsData(); // huoban拥有的战斗ai
	public float getLevelfactor(); // 技能等级系数
	public int getLevelconstant(); // 技能等级常数

	public void setHuobanname(String _v_); // 名字
	public void setHuobannameOctets(com.locojoy.base.Octets _v_); // 名字
	public void setLevel(int _v_); // 等级
	public void setSchool(int _v_); // 职业
	public void setShape(int _v_); // 造型Id
	public void setBodytype(float _v_); // 大小
	public void setSkillmaster(int _v_); // 精通技能
	public void setLevelfactor(float _v_); // 技能等级系数
	public void setLevelconstant(int _v_); // 技能等级常数
}
