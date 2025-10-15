/**
 * 定时补充怪物的定时任务
 */


package fire.pb.activity.timernpc;

import java.util.TimerTask;

public class TimerNpcAddTask extends TimerTask {
	//从定时刷新表里读出来的id
	private final int taskId;
	
	public TimerNpcAddTask(final int taskid) {
		this.taskId = taskid;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				TimerNpcService.getInstance().addTimerNpc(taskId);
				return true;
			}
			
		}.submit();
	}

}
