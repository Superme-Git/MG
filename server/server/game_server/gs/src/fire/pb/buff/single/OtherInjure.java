package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;

public class OtherInjure extends MagicInjure
{
	public OtherInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	//检查吸收
	@Override
	protected void checkAbsorb()
	{
		float  absorbOdds = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_OTHER_ODDS);
		if(absorbOdds > 0 && Math.random() < absorbOdds)
		{
			isAborbed = true;
			AbsorbRate = aimfighter.getEffectRole().getAttrById(FightAttrType.ABSORB_OTHER_PCT);
		}
	}
}
