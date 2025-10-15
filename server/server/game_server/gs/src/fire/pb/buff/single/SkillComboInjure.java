package fire.pb.buff.single;


public class SkillComboInjure extends PhysicalInjure
{

	public SkillComboInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	protected boolean isMultiReturnHurt()
	{
		return true;
	}
}
