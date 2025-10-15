package fire.pb.friends;

import fire.pb.timer.AbstractScheduledActivity;

public class OfflineMsgClearActivity extends AbstractScheduledActivity {

	public OfflineMsgClearActivity(int baseId, int id, long startTime, long endTime) {
		super(baseId, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {
		// clear
		new PClearTimeOutProtocol().submit();
		return true;
	}

	@Override
	protected boolean stopActivity() {
		return true;
	}

	@Override
	protected boolean endActivity() {
		return true;
	}
}
