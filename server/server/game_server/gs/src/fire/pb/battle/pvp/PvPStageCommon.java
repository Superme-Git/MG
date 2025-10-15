package fire.pb.battle.pvp;

import java.util.Calendar;

import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;

/**
 * PvP各个阶段的通用实现
 * @author XGM
 */
public class PvPStageCommon {

	public static class NotOpen implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.NOTOPEN;
		}

		@Override
		public void onEnter(PvPControl c) {
			// TODO 自动生成的方法存根
		}

		@Override
		public void onLeave(PvPControl c) {
			// TODO 自动生成的方法存根
		}
	}

	public static class Start implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.START;
		}

		@Override
		public void onEnter(PvPControl c) {
			c.createNpc();
			c.changeStage(EPvPStage.PREPARE);
		}

		@Override
		public void onLeave(PvPControl c) {
			// TODO 自动生成的方法存根
		}
	}

	public static class Prepare implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.PREPARE;
		}

		@Override
		public void onEnter(PvPControl c) {
			// 战斗开启倒计时
			c.createSchedule(1, 0);
		}

		@Override
		public void onLeave(PvPControl c) {
			// TODO 自动生成的方法存根
		}

		@Override
		public void onTick(PvPControl c) {
			if (c.isFightTime()) {
				c.changeStage(EPvPStage.FIGHT);
			}
		}

		@Override
		public boolean onApplyEnter(PvPControl c, final long roleId, int gotoType) {
			return c.doApplyEnter(roleId, gotoType);
		}

		@Override
		public void onApplyLeave(PvPControl c, final long roleId) {
			c.doApplyLeave(roleId);
		}
	}

	public static class Fight implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.FIGHT;
		}

		@Override
		public void onEnter(PvPControl c) {
			// 设置匹配间隔
			c.createSchedule(5, 0);
		}

		@Override
		public void onLeave(PvPControl c) {
			// TODO 自动生成的方法存根
		}

		@Override
		public void onTick(PvPControl c) {
			// 子类实现
		}

		@Override
		public boolean onApplyEnter(PvPControl c, final long roleId, int gotoType) {
			return c.doApplyEnter(roleId, gotoType);
		}

		@Override
		public void onApplyLeave(PvPControl c, final long roleId) {
			c.doApplyLeave(roleId);
		}
	}

	public static class End implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.END;
		}

		@Override
		public void onEnter(PvPControl c) {
			// 进入奖励阶段
			c.changeStage(EPvPStage.AWARD);
		}

		@Override
		public void onLeave(PvPControl c) {
			// TODO 自动生成的方法存根
		}
	}

	public static class Award implements IPvPStage {

		@Override
		public final EPvPStage getStage() {
			return EPvPStage.AWARD;
		}

		@Override
		public void onEnter(PvPControl c) {
			// 今天最后一秒关闭领奖(23:59:59)
			long delay = getDelayTime();
			delay /= 1000;
			c.createSchedule(0, delay);
		}

		@Override
		public void onLeave(PvPControl c) {
			c.close();
		}

		@Override
		public void onTick(PvPControl c) {
			c.removeSchedule();
			c.removeNpc();
			c.changeStage(EPvPStage.NOTOPEN);
		}

		@Override
		public boolean onApplyEnter(PvPControl c, long roleId, int gotoType) {
			MessageMgr.sendMsgNotify(roleId, 160348, null);
			return true;
		}

		public long getDelayTime() {
			long now = System.currentTimeMillis();
			Calendar lastSecondCalendar = DateValidate.getLastSecondCalendar();
			long delay = lastSecondCalendar.getTimeInMillis() - now;
			return delay;
		}
	}
}
