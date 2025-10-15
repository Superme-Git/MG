package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;
import fire.pb.buff.BuffConstant;
/**土属性伤害**/
public class EarthInjure extends MagicInjure
{

	public EarthInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	//吸收检查
	@Override
	protected void checkAbsorb()
	{
		float  absorbOdds = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_EARTH_ODDS);
		if(absorbOdds > 0 && Math.random() < absorbOdds)
		{
			isAborbed = true;
			AbsorbRate = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_EARTH_PCT);
		}
		else if(/*aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_EARTH_ABSORB_FIRST)||*/aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_EARTH_ABSORB_SECOND))
		{
			InjureRate*=0.7; 
		}
	}

}
