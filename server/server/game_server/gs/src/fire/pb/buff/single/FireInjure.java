package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;
import fire.pb.buff.BuffConstant;
/**火属性伤害*/
public class FireInjure extends MagicInjure
{
	public FireInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	/**检查火吸收*/
	@Override
	protected void checkAbsorb()
	{
		float  absorbOdds = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_FIRE_ODDS);
		if(absorbOdds > 0 && Math.random() < absorbOdds)
		{
			isAborbed = true;
			AbsorbRate = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_FIRE_PCT);
		}
		else  if(/*aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FIRE_ABSORB_FIRST)||*/aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FIRE_ABSORB_SECOND))
		{
			InjureRate*=0.7; 
		}
	}

}
