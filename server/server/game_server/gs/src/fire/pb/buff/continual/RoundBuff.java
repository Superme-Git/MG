package fire.pb.buff.continual;


import xbean.Buff;
/**回合型Buff*/
public class RoundBuff extends ConstantlyBuff
{

	public RoundBuff(Buff buffBean)
	{
		super(buffBean);
	}
	
	public RoundBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}
}
