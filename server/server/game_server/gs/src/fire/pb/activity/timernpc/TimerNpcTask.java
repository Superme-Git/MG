package fire.pb.activity.timernpc;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import fire.pb.util.Misc;

public class TimerNpcTask extends TimerTask {
	
	//从定时刷新表里读出来的id
	private final int taskId;
	
	public TimerNpcTask(final int taskid) {
		this.taskId = taskid;
	}

	@Override
	public void run() {
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(taskId);
		if (timerData == null)
			return;
		
		StringBuilder sb = new StringBuilder();
		sb.append("刷怪定时器开始:").append(taskId).append(",").append(timerData.id)
		.append(",间隔时间:").append(timerData.intervalRefresh)
		.append(",是否触发时间随机:").append(timerData.randomtrigger);
		
		TimerNpcService.logger.info(sb.toString());
		
		//不随机
		if (timerData.randomtrigger == 0) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					TimerNpcService.getInstance().createTimerNpc(taskId);
					return true;
				}
			}.submit();
			return;
		}
		
		//从间隔时间里随机一个时间刷怪
		if (timerData.intervalRefresh > 0) {
			int minute = timerData.intervalRefresh / 60;
			
			int randomminute = Misc.getRandomBetween(0, minute - 1);
			StringBuilder newsb = new StringBuilder();
			newsb.append("随机触发时间(分钟):").append(randomminute)
			.append(",活动id:").append(taskId);
			TimerNpcService.logger.info(newsb.toString());
			
			if (randomminute == 0) {
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						TimerNpcService.getInstance().createTimerNpc(taskId);
						return true;
					}
				}.submit();
				return;
			}
			
			mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					new mkdb.Procedure() {
						@Override
						protected boolean process() {
							TimerNpcService.getInstance().createTimerNpc(taskId);
							return true;
						}
					}.submit();
				}
			}, randomminute, TimeUnit.MINUTES);
		}
		
	}

}
