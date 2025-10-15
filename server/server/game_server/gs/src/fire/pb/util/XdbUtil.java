
package fire.pb.util;

import java.util.concurrent.ScheduledFuture;

import xbean.TimerFuture;


public class XdbUtil {

	
	public static long addScheduleTask(ScheduledFuture<?> future) {

		xbean.TimerFuture timerFuture = xbean.Pod.newTimerFuture();
		timerFuture.setFuture(future);
		long key = xtable.Timerfutures.insert(timerFuture);
		return key;
	}

	
	public static boolean cancelScheduleTask(long taskKey) {

		TimerFuture timerFuture = xtable.Timerfutures.get(taskKey);
		// 如果这个task已经不存在了,则直接返回true
		if (timerFuture == null)
			return true;
		ScheduledFuture<?> future = timerFuture.getFuture();
		// 好像被cancel了也属于Done?
		if (future != null && !future.isDone())
			future.cancel(true);
		// 删掉timerfutures表里面对应的条目
		return xtable.Timerfutures.remove(taskKey);

	}
	public static ScheduledFuture<?>  getScheduleTaskFuture(long taskKey){
		TimerFuture timerFuture = xtable.Timerfutures.get(taskKey);
		if (timerFuture == null)
			return null;
		 return timerFuture.getFuture();
	}
}

