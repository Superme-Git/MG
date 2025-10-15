
package fire.pb.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class DelayActivityManager {

	private static DelayActivityManager instance = new DelayActivityManager();

	private static List<DelayActivity> activityList = new ArrayList<DelayActivity>();

	private DelayActivityManager() {

	}

	public static DelayActivityManager getInstance() {

		return instance;
	}

	public synchronized boolean addDelayActivity(int mapid, long ownerid, Class<? extends DelayActivityThread> delayclass, long endTime) {

		long currentTime = System.currentTimeMillis();
		if (currentTime >= endTime)
			return false;
		DelayActivity activity = new DelayActivity(mapid, ownerid, delayclass, endTime);
		Future<?> future = mkdb.Mkdb.executor().schedule(new CancelDelayActivity(activity), endTime - currentTime, TimeUnit.MILLISECONDS);
		activity.setFuture(future);
		activityList.add(activity);
		return true;
	}

	public synchronized void removeDelayActivity(DelayActivity activity) {

		activityList.remove(activity);
	}
	
	//同步,防止一个activity被两个procedure调用.
	public synchronized void executeDelayActivity(long sceneid,long ownerid,int sceneType) {
        List<DelayActivity> executedActivities = new ArrayList<DelayActivity>();
		for (DelayActivity activity : activityList) {
        	DelayActivityThread activityThread=activity.getTriggerEventThread();
        	if (activityThread==null)
        		continue;
        	if (activityThread.sceneMatch(sceneid, ownerid)) {
        		executedActivities.add(activity);
        		activityThread.setSceneid(sceneid);
        		activityThread.start();
        	}
		}
		activityList.removeAll(executedActivities);
	}
}
