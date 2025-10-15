package fire.pb.skill.fight;

import java.util.List;

import fire.pb.battle.Fighter;
import fire.pb.battle.ResultItem;
import fire.pb.buff.BuffConstant;
import fire.pb.item.ItemBase;
import fire.pb.option.BasicOperation;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.SubSkillConfig;
import xbean.BattleInfo;

public class FightUseItem extends FightSkill
{
	private ItemBase basicitem = null; 
	private final List<Integer> monsterIds;
	

	public FightUseItem(BattleInfo battle, int operator, int aim, int skillId, int type, int targettype, ItemBase basicitem, List<Integer> monsterIds)
	{
		super(battle, operator, aim, skillId, type);
		this.basicitem = basicitem;
		this.monsterIds = monsterIds;
	}

	public ItemBase getBasicItem()
	{
		return basicitem;
	}
	
	@Override
	protected boolean checkSkill()
	{
		return true;
	}
	
	@Override
	protected boolean checkRelation()
	{
		return true;
	}
	
	@Override
	protected void checkDoubleSkill(){}
	@Override
	protected int getFailAimMsg()
	{
		boolean isrelive = false;
		
		for (SubSkillConfig subskill : skillConfig.getSubSkills())
		{
			for (BuffUnit arg : subskill.getBuffUnits())
			{
				if(arg != null && arg.buffIndex == BuffConstant.INSTANT_ITEM_RELIVE)
				{
					isrelive = true;
				}
			}
		}
		if(aimfighter == null)
			return BasicOperation.FAIL_ITEM_AIM;
		
		if((aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)) 
				&& !isrelive)
			return BasicOperation.FAIL_ITEM_TO_DEATH;
		
		return BasicOperation.FAIL_ITEM_AIM;
	}
	@Override
	protected boolean checkAimFighter(Fighter fighter, List<Integer> buffIds, boolean examdead, boolean examhide)
	{
		if(!monsterIds.isEmpty())
		{
			if(fighter.getFightertype() < xbean.Fighter.FIGHTER_PARTNER)
				return false;
			xbean.Monster xmonster = getBattle().getMonsters().get(fighter.getFighterId());
			if(xmonster == null || !monsterIds.contains(xmonster.getMonsterid()))
				return false;
		}
		
		return super.checkAimFighter(fighter, buffIds,examdead, examhide);
	}
	
	
	private ResultItem processBattleNpcItem()
	{
		return null;
	}
}
