package fire.pb.battle.pvp3;

import java.util.concurrent.TimeUnit;

import fire.pb.talk.MessageMgr;
import fire.pb.timer.AbstractScheduledActivity;

/**
 * 3v3活动
 * @author XGM
 */
public class PvP3Activity extends AbstractScheduledActivity {

	public PvP3Activity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {

		// 这里可以进行预告广播
		// 预告
		mkio.Protocol p = MessageMgr.getMsgNotify(160357, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		if (PvP3Control.getInstance().isTestMode() == false) {
			// 1分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160357), 1, TimeUnit.MINUTES);
			// 2分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160357), 2, TimeUnit.MINUTES);
			// 3分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160357), 3, TimeUnit.MINUTES);
			// 4分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160357), 4, TimeUnit.MINUTES);

			// 10分钟后再广播
			mkdb.Executor.getInstance().schedule(new SendAll(160358), 10, TimeUnit.MINUTES);
			// 15分钟后再广播
			mkdb.Executor.getInstance().schedule(new SendAll(160358), 15, TimeUnit.MINUTES);
			// 30分钟后再广播
			mkdb.Executor.getInstance().schedule(new SendAll(160358), 30, TimeUnit.MINUTES);
		}

		// 开启活动
		long now = System.currentTimeMillis();
		if (PvP3Control.getInstance().isTestMode()) {
			PvP3Control.getInstance().init(now, activityEndTime, now, activityEndTime);
		} else {
			PvP3Control.getInstance().init(now, activityEndTime, now + 5 * 60 * 1000, activityEndTime);
		}
		return true;
	}

	@Override
	protected boolean stopActivity() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected boolean endActivity() {

		// 通知客户端活动结束
		mkio.Protocol p = MessageMgr.getMsgNotify(160359, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		// 进入结束阶段
		PvP3Control.getInstance().end();

		// 记录日志
		if (PvP3Control.getLogger().isInfoEnabled()) {
			PvP3Control.getLogger().info("PVP3::[PvP3Activity.endActivity] .");
		}
		return true;
	}

	/**
	 * 全服广播消息
	 * @author XGM
	 */
	private static class SendAll implements Runnable {

		final private int msgId;

		public SendAll(int msgId) {
			this.msgId = msgId;
		}

		@Override
		public void run() {
			mkio.Protocol p = MessageMgr.getMsgNotify(msgId, -1, null);
			gnet.link.Onlines.getInstance().broadcast(p, 999);
		}
	}
}
