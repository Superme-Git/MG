
package fire.pb.timer;


public class ActivityEndThread implements Runnable {

	private final int id;
	private boolean rmv;

	public ActivityEndThread(int id,boolean rmv) {

		super();
		this.id = id;
		this.rmv = rmv;
	}

	@Override
	public void run() {

		// 根据id拿到每个活动的具体实例,并执行end方法
		AbstractScheduledActivity activity = fire.pb.timer.ActivityManager.getActivitymap().get(id);
		try {
			if (activity != null) {
				activity.end(rmv);
				if (rmv) 
				   fire.pb.timer.ActivityManager.getActivitymap().remove(id);
			}
		} catch (Exception e) {

			IScheduledActivity.logger.error("Exception happened when ending activity:" + id, e);

		}
	}

}
