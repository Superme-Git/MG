package fire.pb.buff.continual;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;

public class PeriodBuffTimer extends BuffTimer
{
	
	protected int count;
	protected long initialDelay;
	/***/
	public PeriodBuffTimer(long id, int buffId,long initialDelay, long period,int count)
	{
		super(id, buffId, period);
		this.initialDelay =initialDelay;
		this.count = count;
	}


	/**输入总时间的回合执行buff，仍然是按次数执行*/
	public PeriodBuffTimer(long id, int buffId,long initialDelay, long period,long totalTime)
	{
		super(id, buffId, period);
		this.initialDelay =initialDelay;
		if(totalTime < initialDelay)
			this.count = -1;
		else
			count = ((Long)(1 + (totalTime - initialDelay)/period)).intValue();
	}
	
	@Override
	public boolean schedule()
	{
		if(time <= 0)
			return false;
		future = mkdb.Mkdb.executor().scheduleAtFixedRate(this, initialDelay, time, java.util.concurrent.TimeUnit.MILLISECONDS );
		return true;
	}
	
	@Override
	public void run()
	{	
		perform();
		count--;
		if(count == 0)
		{
			end();
		}
	}
	
	public void perform()
	{//执行周期任务
		new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				BuffAgent agent = new BuffRoleImpl(roleid);

				if (agent.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				{
					agent.processCBuffWhileBattleEnd(buffId);
				} else
				{
					agent.processCBuffWithSP(buffId);
				}
				return true;
			}
		}.submit();
	}
	
	public boolean cancel()
	{
		return future.cancel(true);
	}
	
	public boolean isCancelled ()
	{
		return future.isCancelled();
	}
	
	public boolean isDone()
	{
		return future.isDone();
	}
	
	public long getDelay(java.util.concurrent.TimeUnit tu)
	{
		return future.getDelay(tu);
	}
}
