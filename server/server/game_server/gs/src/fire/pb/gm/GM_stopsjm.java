package fire.pb.gm;

import fire.pb.timer.AbstractScheduledActivity;

public class GM_stopsjm extends GMCommand {
	@Override
	boolean exec(String[] args) {
		try {
			AbstractScheduledActivity activity = fire.pb.timer.ActivityManager
					.getActivitymap().get(1);
			if (activity == null) {
				sendToGM("神机门活动尚未开始,无法停止");
				return true;
			}
			activity.stop(true);
			fire.pb.timer.ActivityManager.getActivitymap().remove(1);
		} catch (Exception e) {

			e.printStackTrace();

		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
