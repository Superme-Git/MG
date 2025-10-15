package fire.pb.buff.continual;

import java.util.concurrent.ScheduledFuture;

/**buff计时器*/
public class BuffTimer implements Runnable
{
	final protected long roleid;
	final protected int buffId;
	protected long time;
	protected ScheduledFuture<?> future;
	
	public BuffTimer(long roleid,int buffId,long time)
	{
		this.roleid = roleid;
		this.buffId = buffId;
		this.time = time;
	}
	
	/**在Procedure中调用*/
	public boolean schedule()
	{
		if(time<=0)
			return false;
		future = mkdb.Mkdb.executor().schedule(this, time,java.util.concurrent.TimeUnit.MILLISECONDS );
		return true;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		perform();
		end();
	}
	
	public void perform()	
	{
		
	}
	
	public void end()
	{
		new PBuffEnd(roleid, buffId).submit();
	}
	
	public boolean cancel()
	{
		return future.cancel(false);
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
	
	public long getSecDelay()
	{
		return future.getDelay(java.util.concurrent.TimeUnit.SECONDS);
	}



	public int getBuffType()
	{
		return buffId;
	}


	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public ScheduledFuture<?> getFuture()
	{
		return future;
	}

}
