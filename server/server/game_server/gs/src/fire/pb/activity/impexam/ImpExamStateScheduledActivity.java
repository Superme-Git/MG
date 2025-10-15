package fire.pb.activity.impexam;

import fire.pb.timer.AbstractScheduledActivity;

/**
 * 智力试练全省统考
 *
 */
public class ImpExamStateScheduledActivity extends AbstractScheduledActivity{

	public ImpExamStateScheduledActivity(int baseid, int id, long startTime,
			long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {
		ImpExamManager.getInstance().initStateExam( activityStartTime, activityEndTime);
		return true;
	}
	
	@Override
	protected long resumeActivity(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception {
		long cur = java.util.Calendar.getInstance().getTimeInMillis();
		if(activityEndTime > cur){
			ImpExamManager.getInstance().initStateExamResume(activityStartTime, activityEndTime);
		}
		return activityEndTime - cur;
	}

	@Override
	protected boolean stopActivity() {
		ImpExamManager.getInstance().endImpExam();
		return true;
	}

	@Override
	protected boolean endActivity() {
		ImpExamManager.getInstance().endStateExam();
		ImpExamManager.getInstance().endImpExam();
		return true;
	}

}
