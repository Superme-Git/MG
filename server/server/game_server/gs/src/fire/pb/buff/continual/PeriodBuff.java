package fire.pb.buff.continual;

import fire.pb.buff.BuffAgent;
import fire.pb.skill.Result;
import xbean.Buff;
/**周期buff*/
public class PeriodBuff extends ConstantlyBuff
{
	public PeriodBuff(Buff buffBean)
	{
		super(buffBean);
	}

	public PeriodBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
		buffBean.setValue(buffConfig.getInitDelay());
	}
	
	public Result process(BuffAgent buffAgent)
	{
		return new Result(true);
	}
	
	public void setInitialDelay(long initialDelay)
	{
		buffBean.setValue(initialDelay);
	}
	
	public long getInitialDelay()
	{
		return buffBean.getValue();
	}
	
	public void setPeriod(long period)
	{
		buffBean.setImpacttime(period);
	}
	
	public long getPeriod()
	{
		return buffBean.getRemaintime();
	}
	
	public void setPeriodCount(int count)
	{
		buffBean.setRound(count);
	}

	public int getPeriodCount()
	{
		return buffBean.getRound();
	}
}
