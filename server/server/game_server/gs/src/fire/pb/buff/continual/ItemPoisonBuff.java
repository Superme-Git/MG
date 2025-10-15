package fire.pb.buff.continual;

import fire.pb.battle.DemoBuff;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.Result;
import xbean.BattleInfo;
import xbean.Buff;

public class ItemPoisonBuff extends RoundBuff
{

	public ItemPoisonBuff(Buff buffBean)
	{
		super(buffBean);
	}
	
	public ItemPoisonBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public DemoResult onRoundEnd(BattleInfo battleInfo, Fighter fighter)
	{
		DemoResult demo = new DemoResult();
		demo.targetid = fighter.getFighterId();
		long sub = 10 + buffBean.getValue()/7;
		if(sub >= buffBean.getValue())
		{
			Result result = fighter.getBuffAgent().removeCBuff(buffId);
			if(result.isSuccess())
				fire.pb.buff.Module.updateDemoResultFromResult(demo, result,fighter);
		}
		else
		{
			buffBean.setValue(buffBean.getValue() - sub);
			demo.demobuffs.add(new DemoBuff(fighter.getFighterId(), buffId, 0));
		}
		return demo;
	}
	
}
