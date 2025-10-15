package fire.pb.activity.timernpc;

import fire.pb.timer.AbstractScheduledActivity;

public class TimerNpcActivity extends AbstractScheduledActivity {

	public TimerNpcActivity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {
		TimerNpcService.getInstance().activityStart(activityStartTime, activityEndTime, this.id, this.baseid);
		return true;
	}

	@Override
	protected boolean stopActivity() {
		TimerNpcService.getInstance().activityStop(this.id);
		return true;
	}

	@Override
	protected boolean endActivity() {
		TimerNpcService.getInstance().activityStop(this.id);
		return true;
	}

}
