
package fire.pb.timer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ActivityResumeThread implements Runnable {

	private final int id;

	private final boolean gm;

	private final long lastStartTime;

	private final long lastEndTime;

	private final long costTime;

	public ActivityResumeThread(int id, boolean gm, long lastStartTime, long lastEndTime, long costTime) {

		super();
		this.id = id;
		this.gm = gm;
		this.lastStartTime = lastStartTime;
		this.lastEndTime = lastEndTime;
		this.costTime = costTime;
	}


	@Override
	public void run() {
		  //根据id拿到每个活动的具体实例,并执行start方法
		 AbstractScheduledActivity activity=fire.pb.timer.ActivityManager.getActivitymap().get(id);
		 try {
			if(activity!=null){
				long delay = activity.resume(gm, lastStartTime, lastEndTime, costTime);
				if (delay>0){
					ScheduledFuture<?> endFuture = mkdb.Mkdb.executor().schedule(new ActivityEndThread(id,true), delay, TimeUnit.MILLISECONDS);
					activity.setEndActivityFuture(endFuture);
				}
			}
		} catch (Exception e) {
			
			IScheduledActivity.logger.error("Exception happened when resuming activity:"+id,e);
			
		}
	}

}
