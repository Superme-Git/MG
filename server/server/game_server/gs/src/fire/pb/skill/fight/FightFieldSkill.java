package fire.pb.skill.fight;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.util.Misc;
import xbean.BattleInfo;

public class FightFieldSkill extends FightSkill
{

	public FightFieldSkill(BattleInfo battle, int operator, int aim, int skillId, int type,int level)
	{
		super(battle, operator, aim, skillId, type);
		this.skillLevel = level;
		opfighter = battle.getFieldfighter();
	}

	@Override
	protected void setJSEngineArgs()
	{
		battle.getEngine().setSkillLevel(getLevel());
		battle.getEngine().setMainDamage(0);
	}

	//能否使用
	@Override
	public boolean canCast()
	{
		if(aim == 0)
		{
			List<Integer> aims = new ArrayList<Integer>();
			aims.addAll(battle.getFighters().keySet());
			Misc.randomlizeList(aims);
			if(aims.size() > 0)
				aim = aims.get(0);
		}
		if (aim <= 0)
			return false;
		aimfighter = battle.getFighterobjects().get(aim);
		if (aimfighter == null)
			return false;
		
		if(!checkMainBuffTarget())
			return false;
		return true;
	}
	
	//无消耗
	@Override
	protected void setResultItemExecuteAndConsume()
	{
		item.execute.attackerid = getOperator();
		item.execute.operationtype = OperationType.ACTION_SKILL;
		item.execute.operationid = getSkillId();
	}
	
	//隐身无效
	protected boolean checkHidden(Fighter fighter){	return true;}
	
	@Override
	protected void processSkillFailed() {}
	// 计算是否触发法术连击
	@Override
	protected void checkDoubleSkill() {}
	@Override
	protected void setRestState(){};
}
