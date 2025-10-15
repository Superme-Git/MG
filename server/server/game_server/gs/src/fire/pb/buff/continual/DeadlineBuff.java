package fire.pb.buff.continual;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import xbean.Buff;

/**回合末清除负面状态*/
public class DeadlineBuff extends RoundBuff
{

	public DeadlineBuff(Buff buffBean)
	{
		super(buffBean);
	}
	
	public DeadlineBuff(ConstantlyBuffConfig config)
	{
		super(config);
	}

	@Override
	public DemoResult onRoundEnd(xbean.BattleInfo battleInfo,Fighter fighter)
	{
		DemoResult demo = super.onRoundEnd(battleInfo,fighter);
		int count = buffBean.getRound();
		if(demo!=null&&count == 0)
		{
			Integer result = battleInfo.getRoundresult().get(fighter.getFighterId());
			result = (result == null)? 0 : result;
			battleInfo.getRoundresult().put(fighter.getFighterId(), result | ResultType.RESULT_SUMMONBACK);
			demo.targetresult |= ResultType.RESULT_DEATH;
			demo.targetresult |= ResultType.RESULT_SUMMONBACK;
		}
		return demo;		
	}
	
}
