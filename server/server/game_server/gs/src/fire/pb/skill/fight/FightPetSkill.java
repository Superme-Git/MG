package fire.pb.skill.fight;

import xbean.BattleInfo;

public class FightPetSkill extends FightSkill
{

	public FightPetSkill(BattleInfo battle, int operator, int aim, int skillId, int type)
	{
		super(battle, operator, aim, skillId, type);
	}

	@Override
	public int getLevel()
	{
		return 1;
	}
		
	@Override
	public int getMPconsume()
	{
		if (opfighter.isDoubleSkill())
			return 0;
		Double result = -skillConfig.getMpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
		return result.intValue();
	}

	
}
