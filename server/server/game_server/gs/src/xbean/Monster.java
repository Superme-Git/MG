
package xbean;

public interface Monster extends mkdb.Bean {
	public Monster copy(); // deep clone
	public Monster toData(); // a Data instance
	public Monster toBean(); // a Bean instance
	public Monster toDataIf(); // a Data instance If need. else return this
	public Monster toBeanIf(); // a Bean instance If need. else return this

	public final static int BATTLE_TITLE_ID = 2; // 战斗内称谓id

	public int getMonsterid(); // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	public int getShape(); // 造型Id
	public int getSchool(); // 职业
	public int getLevel(); // 等级
	public float getBodysize(); // 体型大小
	public int getHp(); // 血
	public int getUplimithp(); // 当前气血上限
	public int getMp(); // 蓝
	public int getSp(); // 怒
	public xbean.BasicFightProperties getBfp(); // 基础战斗属性
	public java.util.Map<Integer, Float> getEffects(); // 效果 key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // 效果 key = effect type id
	public java.util.Map<Integer, Float> getFinalattrs(); // 最终属性 key = attr type
	public java.util.Map<Integer, Float> getFinalattrsAsData(); // 最终属性 key = attr type
	public xbean.BuffAgent getBuffagent(); // buff代理
	public java.util.List<xbean.BattleAI> getAis(); // 怪物ai
	public java.util.List<xbean.BattleAI> getAisAsData(); // 怪物ai
	public int getFightnpctype(); // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	public int getMonstertype(); // 怪物类型,普通,精英,宝宝,boss,公会boss
	public int getSpecialtype(); // 1控制,2治疗,3惧控,4惧疗,5隐身
	public java.util.List<xbean.MonsterSkill> getSkills(); // value= MonsterSkill
	public java.util.List<xbean.MonsterSkill> getSkillsAsData(); // value= MonsterSkill
	public java.util.List<Integer> getImmunebuffs(); // 该怪物免疫的buffId
	public java.util.List<Integer> getImmunebuffsAsData(); // 该怪物免疫的buffId
	public java.util.Map<Integer, Integer> getExtradata(); // 记录一些额外数据
	public java.util.Map<Integer, Integer> getExtradataAsData(); // 记录一些额外数据

	public void setMonsterid(int _v_); // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	public void setShape(int _v_); // 造型Id
	public void setSchool(int _v_); // 职业
	public void setLevel(int _v_); // 等级
	public void setBodysize(float _v_); // 体型大小
	public void setHp(int _v_); // 血
	public void setUplimithp(int _v_); // 当前气血上限
	public void setMp(int _v_); // 蓝
	public void setSp(int _v_); // 怒
	public void setFightnpctype(int _v_); // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	public void setMonstertype(int _v_); // 怪物类型,普通,精英,宝宝,boss,公会boss
	public void setSpecialtype(int _v_); // 1控制,2治疗,3惧控,4惧疗,5隐身
}
