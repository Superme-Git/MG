package fire.pb.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 
 * 建一个集合，存放TimerFuture
 * @author nobody
 *
 */
public class ScheduledFutureMap
{
	private final static ScheduledFutureMap _instance = new ScheduledFutureMap();
	public synchronized static ScheduledFutureMap getInstance(){return _instance;};
	private ScheduledFutureMap(){}
	
	
	private final Map<Long,ScheduledFuture<?>> futures = new HashMap<Long, ScheduledFuture<?>>();
	private long autokey = 0;
	
	public synchronized  long insert(ScheduledFuture<?> future)
	{
		autokey++;
		futures.put(autokey, future);
		return autokey;
	}
	public synchronized  ScheduledFuture<?> get(long futurekey)
	{
		return futures.get(futurekey);
	}
	public synchronized  ScheduledFuture<?> remove(long futurekey)
	{
		return futures.remove(futurekey);
	}
}
