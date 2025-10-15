package fire.pb.buff.continual;

import java.util.Map;

import fire.pb.attr.EffectType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;
import xbean.Buff;

public class HateDecisionBuff extends DecisionBuff {

	public HateDecisionBuff(Buff buffBean) {
		super(buffBean);
		// TODO 自动生成的构造函数存根
	}
	
	/***/
	public HateDecisionBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	/**调用*/
	@Override
	public xbean.Decision getDecision()
	{
		xbean.Decision decision = xbean.Pod.newDecision();
		Float operator = buffBean.getEffects().get(EffectType.OPERATOR);
		if(operator != null)
			decision.setOperator(operator.intValue());
		Float aim = buffBean.getEffects().get(EffectType.AIM);
		if(aim != null)
			decision.setAim(aim.intValue());
		Float operationtype = buffBean.getEffects().get(EffectType.OPERATE_TYPE);
		if(operationtype != null)
			decision.setOperatetype(operationtype.intValue());
		Float operationid = buffBean.getEffects().get(EffectType.OPERATE_ID);
		if(operationid != null)
			decision.setOperateid(operationid.intValue());		
		return decision;
	}
	
	/***/
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		DemoResult demoResult = new DemoResult();
		demoResult.targetid = aimfighter.getFighterId();
		buffBean.getEffects().put(EffectType.OPERATOR,(float)aimfighter.getFighterId());
		buffBean.getEffects().put(EffectType.AIM,(float)opfighter.getFighterId());
		for(Map.Entry<Integer, JavaScript> entry: effects.entrySet())
		{
			buffBean.getEffects().put(entry.getKey(), entry.getValue().eval(battleInfo.getEngine(),opfighter,aimfighter).floatValue());
		}
		Result result = aimfighter.getBuffAgent().addCBuff(this);
		fire.pb.buff.Module.updateDemoResultFromResult(demoResult, result,aimfighter);
		return demoResult;
	}	
}
