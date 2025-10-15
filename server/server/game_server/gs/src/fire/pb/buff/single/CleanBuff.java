package fire.pb.buff.single;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.buff.Module;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class CleanBuff extends SingleBuff
{
	public CleanBuff(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		demoresult = new DemoResult();
		demoresult.targetid = aimfighter.getFighterId();
		Result result = new Result(true);
		if(buffConfig.getEffects().size() == 0)
		{//直接清除
			List<Integer> dispelBuffId = new ArrayList<Integer>();
			//清除不仅有按ID清除，还有按类型清除
			for(int buffId : aimfighter.getBuffAgent().getAllBuffBeans().keySet())
			{
				int bufftype = Module.getInstance().getDefaultCBuffConfig(buffId).getBuffType();
				for(int targetbuff :buffConfig.getTargetBuffs())
				{
					if(targetbuff >= 1000)//按id清除
					{
						if(targetbuff == buffId)
							dispelBuffId.add(buffId);
					}
					else if(targetbuff >= 100 )//按小类型
					{
						if(targetbuff == bufftype)
							dispelBuffId.add(buffId);
					}
					else if(targetbuff >= 10)//按中类型
					{
						if(targetbuff == bufftype / 10)
							dispelBuffId.add(buffId);
					}
					else if(targetbuff >= 1)////按大类型
					{
						if(targetbuff == bufftype / 100)
							dispelBuffId.add(buffId);
					}
				}
				
			}
			for(int buffId : dispelBuffId)
			{
				result.updateResult(aimfighter.getBuffAgent().removeCBuff(buffId));
			}
		}
		else
		{//按值清除
			for(int targetbuffId :buffConfig.getTargetBuffs())
			{
				if(aimfighter.getBuffAgent().existBuff(targetbuffId))
				{
					result.updateResult(aimfighter.getBuffAgent().removeCBuff(targetbuffId,buffConfig.getEffects()));
				}
			}
		}
		if(result.isSuccess())
		{	
			//转换
			fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result,aimfighter);
		}
		
		return demoresult;
	}


}
