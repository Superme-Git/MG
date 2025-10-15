package fire.pb.gm;

import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import fire.pb.main.ConfigManager;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.ActivityEndThread;
import fire.pb.timer.ActivityManager;
import fire.pb.timer.ActivityStartThread;

public class GM_startact extends GMCommand {
	Properties prop = ConfigManager.getInstance().getPropConf("activity");

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		int actId = Integer.parseInt(args[0]);
		int baseid = 0;
		if (actId > 1000)
			baseid = actId / 1000;
		else
			baseid = actId;

		String className = prop.getProperty(String.valueOf(baseid));
		try {
			if (className != null) {
				long startTime = System.currentTimeMillis();

				long endTime = startTime + 7200000;
				int time = 0;
				if (args.length >= 2) {
					time = Integer.parseInt(args[1]);
					endTime = startTime + time * 60 * 1000;
				}

				@SuppressWarnings("unchecked")
				Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class
						.forName(className).getConstructor(int.class,
								int.class, long.class, long.class);
				AbstractScheduledActivity activityInstance = con.newInstance(
						baseid, actId, startTime, endTime);
				ActivityManager.getActivitymap().put(actId, activityInstance);
				mkdb.Mkdb.executor().schedule(
						new ActivityStartThread(actId, endTime - startTime),
						1000, TimeUnit.MILLISECONDS);
				mkdb.Mkdb.executor().schedule(new ActivityEndThread(actId, true),
						endTime - System.currentTimeMillis(),
						TimeUnit.MILLISECONDS);

			}
		} catch (Exception e) {
			logger.error("start activity by GM Command failed.ID:" + actId, e);

		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
