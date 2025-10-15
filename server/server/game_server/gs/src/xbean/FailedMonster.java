
package xbean;

public interface FailedMonster extends mkdb.Bean {
	public FailedMonster copy(); // deep clone
	public FailedMonster toData(); // a Data instance
	public FailedMonster toBean(); // a Bean instance
	public FailedMonster toDataIf(); // a Data instance If need. else return this
	public FailedMonster toBeanIf(); // a Bean instance If need. else return this

	public int getFighterid(); // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
	public boolean getIshost(); // 主方or客方怪物
	public int getMonsterid(); // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	public int getLevel(); // 等级
	public int getShape(); // 造型Id
	public int getFightnpctype(); // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	public int getMonstertype(); // 怪物类型,普通,精英,宝宝,boss,公会boss
	public int getResult(); // 结束时的结果，参考fire.pb.battle.ResultType

	public void setFighterid(int _v_); // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
	public void setIshost(boolean _v_); // 主方or客方怪物
	public void setMonsterid(int _v_); // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	public void setLevel(int _v_); // 等级
	public void setShape(int _v_); // 造型Id
	public void setFightnpctype(int _v_); // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	public void setMonstertype(int _v_); // 怪物类型,普通,精英,宝宝,boss,公会boss
	public void setResult(int _v_); // 结束时的结果，参考fire.pb.battle.ResultType
}
