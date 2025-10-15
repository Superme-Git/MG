package fire.pb.mission.instance;

import fire.pb.timer.AbstractScheduledActivity;

public class InstanceZoneActivity extends AbstractScheduledActivity {

	public InstanceZoneActivity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {
		InstanceManager.getInstance().instanceStart(activityStartTime, activityEndTime, this.id, this.baseid);
		return false;
	}

	@Override
	protected boolean stopActivity() {
		InstanceManager.getInstance().instanceStop(this.id);
		return false;
	}

	@Override
	protected boolean endActivity() {
		InstanceManager.getInstance().instanceStop(this.id);
		return false;
	}

}
