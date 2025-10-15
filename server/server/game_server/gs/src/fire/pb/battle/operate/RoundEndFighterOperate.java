package fire.pb.battle.operate;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fire.pb.attr.AttrType;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
/**回合末动作*/
public class RoundEndFighterOperate extends Operate
{
	private final Fighter fighter;
	public RoundEndFighterOperate(Fighter fighter)
	{
		this.fighter = fighter;
	}
	
	public ResultItem process(ListIterator<Operate> lit)
	{
		ResultItem item = new ResultItem();
		item.execute.attackerid = 0;
		item.execute.operationtype = OperationType.ACTION_ROUNDENDDEMO; 
		SubResultItem subItem = new SubResultItem();
		item.subresultlist.add(subItem);	
		boolean bregfighter = false;
		if(getBattle().getRound() > 0)
		{
			List<ConstantlyBuff> roundbuffs = fighter.getBuffAgent().getRoundBuffs();
			for (int i = 0; i < BuffConstant.ROUND_END_PROCESS_BUFF_TYPES.length; i++)
			{
				for (Iterator<ConstantlyBuff> it = roundbuffs.iterator(); it.hasNext();)
				{
					ConstantlyBuff buff = it.next();
					int hotdottype = buff.getBuffConfig().getBuffType() / 10;
					if (hotdottype == BuffConstant.ROUND_END_PROCESS_BUFF_TYPES[i] && fighter.getBuffAgent().existBuff(buff.getId()))
					{
						if (!checkAimFighter(getBattle(), fighter, buff.getId()))
							continue;
						if(bregfighter == false)
						{
							getBattle().getEngine().setOpFighter(fighter);// 设置技能释放者参数
							getBattle().getEngine().setSkillLevel(fighter.getEffectRole().getLevel());// 设置技能相关参数
							bregfighter = true;
						}
						DemoResult demo = buff.onRoundEnd(getBattle(), fighter);
						if (demo != null)
						{
							if((demo.targetresult & ResultType.RESULT_HURT) > 0 && (demo.targetresult & ResultType.RESULT_DEATH) == 0 )
								demo.targetresult -= ResultType.RESULT_HURT;
							subItem.resultlist.add(demo);
						}
						it.remove();
						if (hotdottype == BuffConstant.TYPE_FATHER_CONTINUAL_DOT)
						{
							getBattle().setBattleresult(BattleField.checkBattleEnd(getBattle()));
							if (getBattle().getBattleresult() != BattleField.BATTLE_NOT_END)
								return item;
						}
					}
				}
			}
			
			//Round count down
			for (Iterator<ConstantlyBuff> it = roundbuffs.iterator(); it.hasNext();)
			{
				ConstantlyBuff buff = it.next();
				if (!fighter.getBuffAgent().existBuff(buff.getId()))
					continue;
				if(bregfighter == false)
				{
					getBattle().getEngine().setOpFighter(fighter);// 设置技能释放者参数
					getBattle().getEngine().setSkillLevel(fighter.getEffectRole().getLevel());// 设置技能相关参数
					bregfighter = true;
				}
				DemoResult demotmp = buff.onRoundEnd(getBattle(), fighter);
				if (demotmp != null)
				{
					if((demotmp.targetresult & ResultType.RESULT_DEATH) > 0)
						demotmp.targetresult |= ResultType.RESULT_HURT;
					else if((demotmp.targetresult & ResultType.RESULT_HURT) > 0 )
						demotmp.targetresult -= ResultType.RESULT_HURT;
					subItem.resultlist.add(demotmp);
				}
				it.remove();
			}
/*			if((fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)) && 
					fighter.getFighterBean().getInitattrs().get(AttrType.EFFECT_POINT) > 0)
			{
				DemoResult demo = new DemoResult();
				demo.epchange =  -1;
				demo.targetid = fighter.getFighterId();
				demo.flagtype = 0;
				demo.targetresult |= ResultType.RESULT_EPCHANGE;
				fighter.getFighterBean().getInitattrs().put(AttrType.EFFECT_POINT, fighter.getFighterBean().getInitattrs().get(AttrType.EFFECT_POINT)-1);
				subItem.resultlist.add(demo);
			}*/

		}
		if(subItem.resultlist.isEmpty())
			return null;
		return item;
	}
	
	private static boolean checkAimFighter(xbean.BattleInfo battle, Fighter fighter, int buffId)
	{
		if (!battle.getFighters().containsKey(fighter.getFighterId()))
			return false;
		if (BattleField.checkOutBattle(battle, fighter.getFighterId()))
			return false;
		if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
		{
			if (buffId != BuffConstant.INSTANT_RELIVE && buffId != BuffConstant.INSTANT_ITEM_RELIVE)
				return false;
		}
		if (!fighter.getBuffAgent().canAddBuff(buffId))
			return false;
		return true;
	}
}
