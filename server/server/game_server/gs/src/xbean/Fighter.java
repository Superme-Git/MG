
package xbean;

public interface Fighter extends mkdb.Bean {
	public Fighter copy(); // deep clone
	public Fighter toData(); // a Data instance
	public Fighter toBean(); // a Bean instance
	public Fighter toDataIf(); // a Data instance If need. else return this
	public Fighter toBeanIf(); // a Bean instance If need. else return this

	public final static int DrugStuffID = 275; // 
	public final static int DrugStuffID_LV2 = 323; // 2级药品
	public final static int FoodID = 290; // 食品
	public final static int DrugID = 291; // 药品
	public final static int DrugStuffTime = 20; // 药材
	public final static int DrugFoodTime = 10; // 食品药品
	public final static int FIGHTER_ROLE = 1; // 角色
	public final static int FIGHTER_PET = 2; // 宠物
	public final static int FIGHTER_PARTNER = 3; // 伙伴（玩家自带助战）
	public final static int FIGHTER_MONSTER_HIDE = 4; // 暗雷野怪（20000~23999）
	public final static int FIGHTER_MONSTER_NPC = 5; // 战斗npc（24000以上）
	public final static int FIGHTER_SYSTEM_PARTNER = 6; // 系统安排的助战
	public final static int PHY_ATTACK = 1; // 一场战斗进行了多少次物理攻击，包括多次技能连击、合击
	public final static int PHY_HURT = 2; // 一场战斗受到了多少次物理伤害，包括保护
	public final static int MAGIC_ATTACK = 3; // 一场战斗进行了多少次法术攻击
	public final static int MAGIC_HURT = 4; // 一场战斗受到了多少次法术伤害

	public long getUniqueid(); // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
	public String getFightername(); // 名称
	public com.locojoy.base.Octets getFighternameOctets(); // 名称
	public int getSubtype(); // 子类型
	public int getFightertype(); // 战斗单位的类型
	public int getBattleindex(); // 战斗者的战斗单位索引，即fighterId
	public int getFighterkey(); // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	public boolean getIshost(); // 是否是战斗的主方
	public int getRound(); // 战斗者进入战斗的回合数
	public int getPositionx(); // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
	public int getPositiony(); // 战斗者位置y
	public int getInilevel(); // 战斗者的初始等级
	public int getInihp(); // 战斗者的初始hp
	public int getInimp(); // 战斗者的初始mp
	public int getInisp(); // 战斗者的初始怒气
	public int getInipetkey(); // 战斗者的初始宠物key
	public int getFootlogoid(); // 足印效果id
	public int getShapeid(); // 模型id
	public java.util.Map<Integer, Integer> getActioncount(); // 记录一场战斗的法术物理的攻击和受击次数
	public java.util.Map<Integer, Integer> getActioncountAsData(); // 记录一场战斗的法术物理的攻击和受击次数
	public java.util.Map<Integer, Integer> getFightedpets(); // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
	public java.util.Map<Integer, Integer> getFightedpetsAsData(); // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
	public java.util.Map<Integer, Float> getInitattrs(); // 记录角色进入战斗时的初始值
	public java.util.Map<Integer, Float> getInitattrsAsData(); // 记录角色进入战斗时的初始值
	public java.util.Map<Integer, Integer> getInitskills(); // 记录角色进入战斗时的技能初始值
	public java.util.Map<Integer, Integer> getInitskillsAsData(); // 记录角色进入战斗时的技能初始值
	public java.util.Map<Integer, Integer> getUseditems(); // 记录在此场战斗中用过的物品
	public java.util.Map<Integer, Integer> getUseditemsAsData(); // 记录在此场战斗中用过的物品

	public void setUniqueid(long _v_); // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
	public void setFightername(String _v_); // 名称
	public void setFighternameOctets(com.locojoy.base.Octets _v_); // 名称
	public void setSubtype(int _v_); // 子类型
	public void setFightertype(int _v_); // 战斗单位的类型
	public void setBattleindex(int _v_); // 战斗者的战斗单位索引，即fighterId
	public void setFighterkey(int _v_); // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	public void setIshost(boolean _v_); // 是否是战斗的主方
	public void setRound(int _v_); // 战斗者进入战斗的回合数
	public void setPositionx(int _v_); // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
	public void setPositiony(int _v_); // 战斗者位置y
	public void setInilevel(int _v_); // 战斗者的初始等级
	public void setInihp(int _v_); // 战斗者的初始hp
	public void setInimp(int _v_); // 战斗者的初始mp
	public void setInisp(int _v_); // 战斗者的初始怒气
	public void setInipetkey(int _v_); // 战斗者的初始宠物key
	public void setFootlogoid(int _v_); // 足印效果id
	public void setShapeid(int _v_); // 模型id
}
