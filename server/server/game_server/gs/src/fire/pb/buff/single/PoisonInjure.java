package fire.pb.buff.single;

import fire.pb.attr.EffectType;


/**毒伤害*/
public class PoisonInjure extends Injure
{

	public PoisonInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	protected void processInjure()
	{
		//计算伤害
		damage = Math.min(-1,damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue());
		//处理伤害
		damage = (int)amendPoisonInjure(damage);
		//大健要求对毒的伤害加限制，下限 = -受毒者等级/10；上限 = -受毒者等级 * 16
		demoresult.hpchange = (int) Math.min(-(aimfighter.getEffectRole().getLevel() / 10 +1), Math.max(-(aimfighter.getEffectRole().getLevel() * 16 + 20), damage));
		//处理目标
		handleAim();
	}
	
	protected double amendPoisonInjure(double poisonInjure)
	{
		return amendInjure(poisonInjure);
	}
	
	/**掉蓝处理 */
	@Override
	protected void handleMpChange()
	{
		if(effects.containsKey(EffectType.MP_ABL))
		{
			demoresult.mpchange = effects.get(EffectType.MP_ABL).eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			demoresult.mpchange = (int)randomValueInScale(demoresult.mpchange, 0.95f, 1.05f);
			if(demoresult.mpchange > -1)
				demoresult.mpchange = -1;
		}
		else if(effects.containsKey(EffectType.MP_PCT))
		{
			demoresult.mpchange =  (int)(effects.get(EffectType.MP_PCT).eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMp());
			if(demoresult.mpchange > -1)
				demoresult.mpchange = -1;
		}
		if(demoresult.mpchange != 0)
		{
			demoresult.mpchange = Math.min(-(aimfighter.getEffectRole().getLevel() / 10 +1), Math.max(-(aimfighter.getEffectRole().getLevel() * 16 + 20), demoresult.mpchange));
			aimfighter.attachMpChange(demoresult.mpchange);
		}
	}
	protected void handleAttackCount(){}
	
}
