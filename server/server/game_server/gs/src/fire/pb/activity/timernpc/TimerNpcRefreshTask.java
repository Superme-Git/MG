package fire.pb.activity.timernpc;

import java.util.TimerTask;

public class TimerNpcRefreshTask extends TimerTask {

	//从定时刷新表里读出来的id
	private final int taskId;
	
	public TimerNpcRefreshTask(final int taskid) {
		this.taskId = taskid;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				TimerNpcService.getInstance().refreshTimerNpc(taskId);
				return true;
			}
			
		}.submit();
	}

}
