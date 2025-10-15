package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;
import fire.pb.buff.BuffConstant;
/**雷属性伤害*/

public class ThunderInjure extends MagicInjure
{

	public ThunderInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	/**吸收检查*/
	@Override
	protected void checkAbsorb()
	{
		float  absorbOdds = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_THUNDER_ODDS);
		if(absorbOdds > 0 && Math.random() < absorbOdds)
		{
			isAborbed = true;
			AbsorbRate = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_THUNDER_PCT);
		}
		else if(/*aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_THUNDER_ABSORB_FIRST)||*/aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_THUNDER_ABSORB_SECOND))
		{
			InjureRate*=0.7; 
		}
	}

}
