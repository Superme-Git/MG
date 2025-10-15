package fire.pb.battle.pvp1;

import java.util.concurrent.TimeUnit;

import fire.pb.talk.MessageMgr;
import fire.pb.timer.AbstractScheduledActivity;

/**
 * 1v1活动
 * @author XGM
 */
public class PvP1Activity extends AbstractScheduledActivity {

	public PvP1Activity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {

		// 这里可以进行预告广播
		// 预告
		mkio.Protocol p = MessageMgr.getMsgNotify(160354, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		if (PvP1Control.getInstance().isTestMode() == false) {
			// 1分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160354), 1, TimeUnit.MINUTES);
			// 2分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160354), 2, TimeUnit.MINUTES);
			// 3分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160354), 3, TimeUnit.MINUTES);
			// 4分钟后再预告
			mkdb.Executor.getInstance().schedule(new SendAll(160354), 4, TimeUnit.MINUTES);

			// 10分钟后再广播
			mkdb.Executor.getInstance().schedule(new SendAll(160355), 10, TimeUnit.MINUTES);
			// 15分钟后再广播
			mkdb.Executor.getInstance().schedule(new SendAll(160355), 15, TimeUnit.MINUTES);
		}

		// 开启活动
		long now = System.currentTimeMillis();
		if (PvP1Control.getInstance().isTestMode()) {
			PvP1Control.getInstance().init(now, activityEndTime, now, activityEndTime);
		} else {
			PvP1Control.getInstance().init(now, activityEndTime, now + 5 * 60 * 1000, activityEndTime);
		}

		return true;
	}

	@Override
	protected boolean stopActivity() {
		return false;
	}

	@Override
	protected boolean endActivity() {

		// 通知客户端活动结束
		mkio.Protocol p = MessageMgr.getMsgNotify(160356, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		// 进入结束阶段
		PvP1Control.getInstance().end();

		// 记录日志
		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[SPvPRaceActivity.endActivity] .");
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
