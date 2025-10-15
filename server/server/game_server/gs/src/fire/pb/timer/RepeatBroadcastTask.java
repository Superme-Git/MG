package fire.pb.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;


public class RepeatBroadcastTask extends java.util.TimerTask
{
	final private String msg;
	private int times;//次数
	ScheduledFuture<?> future;
	
	public RepeatBroadcastTask(String msg, int times)
	{
		this.msg = msg;
		this.times = times;
	}
	@Override
	public void run()
	{
		List<String> pars = new LinkedList<String>();
		pars.add(msg);
		fire.pb.talk.MessageMgr.broadcastMsgNotify(141661, 0, pars);
		times--;
		if(times <= 0)
			future.cancel(false);
	}
	
	public void setFuture(ScheduledFuture<?> future)
	{
		this.future = future;
	}

}
