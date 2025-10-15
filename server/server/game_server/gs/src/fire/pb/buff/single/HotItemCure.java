package fire.pb.buff.single;
import fire.pb.buff.BuffConstant;


/**使用物品治疗*/
public class  HotItemCure extends ItemCure
{
	public HotItemCure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
		
	}
	
	@Override
	protected void handleProhibitHeal()
	{
		if(	aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
			isProhibitHeal = true;
	}

}
