package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;
import fire.pb.buff.BuffConstant;
/**冰属水伤害*/
public class WaterInjure extends MagicInjure
{

	public WaterInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	/**吸收检查*/
	@Override
	protected void checkAbsorb()
	{
		float  absorbOdds = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_WATER_ODDS);
		if(absorbOdds > 0 && Math.random() < absorbOdds)
		{
			isAborbed = true;
			AbsorbRate = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_WATER_PCT);
		}
		else if( /*aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_WATER_ABSORB_FIRST)||*/aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_WATER_ABSORB_SECOND))
		{
			InjureRate*=0.7; 
		}
	}

}
