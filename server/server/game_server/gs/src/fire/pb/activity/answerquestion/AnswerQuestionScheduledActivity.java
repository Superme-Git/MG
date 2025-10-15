package fire.pb.activity.answerquestion;

import fire.pb.timer.AbstractScheduledActivity;

/**
 * 答题 by changhao
 * 
 * @see
 */
public class AnswerQuestionScheduledActivity extends AbstractScheduledActivity {

	public AnswerQuestionScheduledActivity(int baseid,int id, long startTime, long endTime) {

		super(baseid,id, startTime, endTime);

	}
	
	@Override
	protected boolean init() throws Exception {
		//KeJuManager.getInstance().initXS( activityStartTime, activityEndTime);
		ActivityQuestionManager.getInstance().init();
		return true;
	}
	
	@Override
	protected long resumeActivity(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception {
		long cur = java.util.Calendar.getInstance().getTimeInMillis();
		if(activityEndTime > cur){
			//KeJuManager.getInstance().initXSResume(activityStartTime, activityEndTime);
			ActivityQuestionManager.getInstance().start();	
		}
		return activityEndTime - cur;
	}
	
	
	@Override
	protected boolean stopActivity() {
		//KeJuManager.getInstance().endKeJu();
		ActivityQuestionManager.getInstance().stop();
		 return true;
	}
	
	@Override
	protected boolean endActivity() {
		//KeJuManager.getInstance().endKeJu();
		ActivityQuestionManager.getInstance().stop();
		return true;
	}

}
