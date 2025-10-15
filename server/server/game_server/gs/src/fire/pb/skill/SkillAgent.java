package fire.pb.skill;

import java.util.List;

public abstract class SkillAgent
{
	protected int agentType;//枚举值对应xbean.Fighter中的FIGHTER_XXX
	
	//检查存在着的技能
	public abstract boolean hasSkill(int skillId);
	//检查生效的技能（对于宠物来说，较高级的技能存在时，低级的不生效）
	public abstract boolean hasEffectSkill(int skillId);	
	//检查多个技能，当所有都拥有时才返回true
	public abstract boolean hasAllSkills(int... skillIds);	
	//检查多个技能，当拥有一个就返回true
	public abstract boolean hasAnySkill(int... skillIds);
	
	public abstract int getLevel(int skillId);
	public abstract List<Integer> getAllBattleSkills();
	public abstract Result addSkillBuffWhileOnline(xbean.BattleInfo battle);
	public abstract Result addSkillBuffWhileBattle(xbean.BattleInfo battle, fire.pb.battle.Fighter ofighter);
	
	//返回代理类型，枚举值对应xbean.Fighter中的FIGHTER_XXX
	public int getAgentType()
	{
		return agentType;
	}
}
