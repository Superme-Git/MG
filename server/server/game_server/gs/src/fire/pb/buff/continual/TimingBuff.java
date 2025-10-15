package fire.pb.buff.continual;

/**没有效果，没有其他特殊处理，只有计时的buff*/
public class TimingBuff extends ConstantlyBuff
{

	public TimingBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	public TimingBuff(xbean.Buff buffBean)	
	{
		super(buffBean);
	}
}
