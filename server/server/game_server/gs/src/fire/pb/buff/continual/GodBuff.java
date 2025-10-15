package fire.pb.buff.continual;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.Result;
import xbean.Buff;

/**宠物的神迹技能buff，回合末驱散所有负面状态*/
public class GodBuff extends RoundBuff
{
	public GodBuff(Buff buffBean)
	{
		super(buffBean);
	}
	
	public GodBuff(ConstantlyBuffConfig config)
	{
		super(config);
	}

	@Override
	public DemoResult onRoundEnd(xbean.BattleInfo battleInfo,Fighter fighter)
	{
		DemoResult demo = new DemoResult();
		demo.targetid = fighter.getFighterId();
		
		List<Integer> rmbuffIds = new ArrayList<Integer>();		
		for(int buffId : fighter.getBuffAgent().getAllBuffBeans().keySet())
		{
			ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
			if(conf.getBuffType() / 100 == 1)//负面buff
			{
				rmbuffIds.add(buffId);
			}
		}
		if(rmbuffIds.size() == 0)
			return null;
			
		Result result = new Result(true);
		for(int buffId : rmbuffIds)
		{
			result.updateResult(fighter.getBuffAgent().removeCBuff(buffId));
		}		
		fire.pb.buff.Module.updateDemoResultFromResult(demo, result,fighter);		
		return demo;
	}
	
}
