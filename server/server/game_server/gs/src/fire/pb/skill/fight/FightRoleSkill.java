package fire.pb.skill.fight;

import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillRole;
import xbean.BattleInfo;

public class FightRoleSkill extends FightSkill
{

	public FightRoleSkill(BattleInfo battle, int operator, int aim, int skillId, int type)
	{
		super(battle, operator, aim, skillId, type);
		SkillRole sbrole = new SkillRole(opfighter.getFighterBean().getUniqueid(), true);
		skillLevel = sbrole.getLevel(skillId);
	}
	
	@Override
	public int getSPconsume()
	{
		Double result = -skillConfig.getSpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
		if(opfighter.getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_LESS_SP_SKILL))
			result = result * 0.8;
		return result.intValue();
	}
	
}
