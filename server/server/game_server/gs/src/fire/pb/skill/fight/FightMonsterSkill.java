package fire.pb.skill.fight;

import fire.pb.skill.SubSkillConfig;
import xbean.BattleInfo;

public class FightMonsterSkill extends FightPetSkill
{

	public FightMonsterSkill(BattleInfo battle, int operator, int aim, int skillId, int type,int skilllevel)
	{
		this(battle, operator, aim, skillId, type);
		this.skillLevel = skilllevel;
	}
	
	public FightMonsterSkill(BattleInfo battle, int operator, int aim, int skillId, int type)
	{
		super(battle, operator, aim, skillId, type);
		xbean.Monster monster = battle.getMonsters().get(operator);
		if(monster !=null)
		{
			for (xbean.MonsterSkill mskill : monster.getSkills())
			{
				if(mskill.getId() == skillId)
				{
					this.skillLevel = mskill.getSkilllevel();
					return;					
				}
			}
		}
		this.skillLevel = 1;
	}
	
	@Override
	protected boolean checkSkill()
	{
		return true;
	}
	
	@Override
	public int getSPconsume()
	{
		return 0;
	}
	
	
	@Override
	public int getLevel()
	{
		return skillLevel;
	}
	
	// buff检查
	@Override
	protected boolean checkClashAndDepend(SubSkillConfig subskill)
	{
		return true;
	}


	
	
}
