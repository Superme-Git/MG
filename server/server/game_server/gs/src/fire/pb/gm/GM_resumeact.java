package fire.pb.gm;

import java.lang.reflect.Constructor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import xtable.Activitytable;
import fire.pb.main.ConfigManager;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.ActivityManager;
import fire.pb.timer.ActivityResumeThread;
import fire.pb.timer.IScheduledActivity;

public class GM_resumeact extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			sendToGM(usage());
			return false;
		}
		Integer actid = Integer.parseInt(args[0]);

		try {
			xbean.ActivityStatus activityStatus = Activitytable.select(actid);
			if (activityStatus == null) {
				sendToGM("未在数据库中找到对应的信息,可能该id的活动之前从未开启过");
				return false;
			}
			String className = ConfigManager.getInstance()
					.getPropConf("activity")
					.getProperty(Integer.toString(activityStatus.getBaseid()));
			@SuppressWarnings("unchecked")
			Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class
					.forName(className).getConstructor(int.class, int.class,
							long.class, long.class);
			AbstractScheduledActivity activityInstance;
			activityInstance = con.newInstance(activityStatus.getBaseid(),
					actid, activityStatus.getInistarttime(),
					activityStatus.getIniendtime());
			ActivityManager.getActivitymap().put(actid, activityInstance);
			long delayTime = 100000;
			ScheduledFuture<?> resumeFuture = mkdb.Mkdb.executor().schedule(
					new ActivityResumeThread(actid, false,
							activityStatus.getLaststarttime(),
							activityStatus.getLastendtime(),
							activityStatus.getCosttime()), delayTime,
					TimeUnit.MILLISECONDS);
			activityInstance.setResumeActivityFuture(resumeFuture);
			return true;
		} catch (Exception e) {
			IScheduledActivity.logger.error(
					"resume activity failed.activity id" + actid, e);
			return true;
		}
	}

	@Override
	String usage() {

		return "resumeact activityid";
	}

}
