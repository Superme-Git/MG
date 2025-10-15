
package fire.pb.timer;

public class ActivityStartThread implements Runnable {

	private final int id ;
	private long period;

	public ActivityStartThread(int id,long period) {
		this.id = id;
		this.period = period;
	}

	@Override
	public void run() {
      //根据id拿到每个活动的具体实例,并执行start方法
	 AbstractScheduledActivity activity=fire.pb.timer.ActivityManager.getActivitymap().get(id);
	 try {
		if(activity!=null){
			if (period > 0){//如果是周循环的活动
                Long curTime = System.currentTimeMillis();				
				activity.setActivityStartTime(curTime);
				activity.setActivityEndTime(curTime + period);
			}
			activity.start();
		}
	} catch (Exception e) {
		
		IScheduledActivity.logger.error("Exception happened when starting activity:"+id,e);
		
	}
	}

}

