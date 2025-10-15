

package fire.pb.npc.NpcAI;

import java.util.concurrent.TimeUnit;

import fire.msp.npc.GCheckNpcMove;
import fire.pb.GsClient;

public class NpcAITimer
{
	
	private class TimeSchedule implements Runnable 
	{
		
		@Override
		public void run() {
			
			final long tick = System.currentTimeMillis();
			GsClient.pSendWhileCommit(new GCheckNpcMove(tick));
		}
	}
	
	public synchronized void start()
	{
		if(null != timer)
			return;
		
		timer = mkdb.Mkdb.executor().scheduleAtFixedRate(new TimeSchedule(), 0, INTERVAL, TimeUnit.MILLISECONDS);
	}
	
	public synchronized void end()
	{
		if(null != timer){
			timer.cancel(true);
			timer = null;
		}
	}
	
	private static final NpcAITimer instance = new NpcAITimer();
	
	public synchronized static NpcAITimer getInstance()
	{
		return instance;
	}
	
	private NpcAITimer() {}
	
	private java.util.concurrent.ScheduledFuture<?> timer = null;
	private static final int INTERVAL = 500;
}