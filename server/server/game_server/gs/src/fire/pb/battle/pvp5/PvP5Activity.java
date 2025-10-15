package fire.pb.battle.pvp5;

import java.util.concurrent.TimeUnit;

import fire.pb.talk.MessageMgr;
import fire.pb.timer.AbstractScheduledActivity;

/**
 * 5v5活动
 * @author XGM
 */
public class PvP5Activity extends AbstractScheduledActivity {

	public PvP5Activity(int baseid, int id, long startTime, long endTime) {
		super(baseid, id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {

		// 这里可以进行预告广播
		// 预告
		mkio.Protocol p = MessageMgr.getMsgNotify(160360, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		if (PvP5Control.getInstance().isTestMode() == false) {
			// 预告(30分钟准备时间,每5分钟预告一次)
			for (int i = 1; i <= 5; i++) {
				mkdb.Executor.getInstance().schedule(new SendAll(160360), i * 5, TimeUnit.MINUTES);
			}

			// 进行中的广播(30分钟后比赛开始,每10分钟广播一次,广播9次)
			for (int i = 0; i < 9; i++) {
				mkdb.Executor.getInstance().schedule(new SendAll(160361), 30 + i * 10, TimeUnit.MINUTES);
			}
		}

		// 开启活动
		long now = System.currentTimeMillis();
		if (PvP5Control.getInstance().isTestMode()) {
			PvP5Control.getInstance().init(now, activityEndTime, now, activityEndTime);
		} else {
			// 30分钟准备时间
			PvP5Control.getInstance().init(now, activityEndTime, now + 30 * 60 * 1000, activityEndTime);
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
		mkio.Protocol p = MessageMgr.getMsgNotify(160362, -1, null);
		gnet.link.Onlines.getInstance().broadcast(p, 999);

		// 进入结束阶段
		PvP5Control.getInstance().end();

		// 记录日志
		if (PvP5Control.getLogger().isInfoEnabled()) {
			PvP5Control.getLogger().info("PVP5::[PvP5Activity.endActivity] .");
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
