package fire.pb.instancezone.timer;

import com.locojoy.base.Runnable;

public class InstanceTimer extends Runnable
{
	final private long landkey;
	final private int timerId;
	
	
	public InstanceTimer(long landkey, int timerId)
	{
		this.landkey = landkey;
		this.timerId = timerId;
	}


	@Override
	public void run()
	{
		new PInstanceTimerProcess(landkey,timerId).submit();
	}

}
