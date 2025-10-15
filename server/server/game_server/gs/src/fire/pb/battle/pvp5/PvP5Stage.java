package fire.pb.battle.pvp5;

import java.util.List;

import fire.msp.IGetRolesCallBack;
import fire.pb.battle.pvp.PvPControl;
import fire.pb.battle.pvp.PvPStageCommon;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneManager;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;

/**
 * 5v5各个阶段的实现
 * @author XGM
 */
public class PvP5Stage {

	public static class NotOpen extends PvPStageCommon.NotOpen {}

	public static class Start extends PvPStageCommon.Start {

		@Override
		public void onEnter(PvPControl c) {

			// 启动
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					for (int grade : PvP5Helper.getAllRaceIds()) {
						PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
						race.begin();
					}
					return true;
				}
			}.submit();

			// 拉人
			RoleManager.getRolesByConditions(-1, PvP5Helper.ENTER_MIN_LEVEL,
					-1, -1, -1, new IGetRolesCallBack() {
						@Override
						public void process(List<Long> roleIds) {
							SGeneralSummonCommand send = new SGeneralSummonCommand();
							send.roleid = 0;
							send.mapid = 0;
							send.minimal = 0;
							send.summontype = TransmitTypes.pvp5;
							mkdb.Procedure.psendWhileCommit(roleIds, send);
						}
					});

			// 通知客户端活动开启
			SActivityOpen so = new SActivityOpen();
			so.activityid = 242;
			SceneManager.sendAll(so);

			super.onEnter(c);
		}
	}

	public static class Prepare extends PvPStageCommon.Prepare {

		@Override
		public void onEnter(PvPControl c) {

			super.onEnter(c);

			// 挂机奖励
			if (c instanceof PvP5Control) {
				PvP5Control pc = (PvP5Control) c;
				pc.createScheduleAward(30, 0, new PvP5ScheduleAward(getStage()));
			}
		}

		@Override
		public void onLeave(PvPControl c) {

			// 取消挂机奖励
			if (c instanceof PvP5Control) {
				PvP5Control pc = (PvP5Control) c;
				pc.removeScheduleAward();
			}

			super.onLeave(c);
		}
	}

	public static class Fight extends PvPStageCommon.Fight {

		@Override
		public void onEnter(PvPControl c) {

			super.onEnter(c);

			// 挂机奖励
			if (c instanceof PvP5Control) {
				PvP5Control pc = (PvP5Control) c;
				pc.createScheduleAward(30, 0, new PvP5ScheduleAward(getStage()));
			}
		}

		@Override
		public void onLeave(PvPControl c) {

			// 取消挂机奖励
			if (c instanceof PvP5Control) {
				PvP5Control pc = (PvP5Control) c;
				pc.removeScheduleAward();
			}

			super.onLeave(c);
		}

		@Override
		public void onTick(PvPControl c) {
			if (c.isFightTime()) {
				// 进行匹配
				for (int grade : PvP5Helper.getAllRaceIds()) {
					new PPvP5Scan(grade).submit();
					new PPvP5PairBattleFromQueue(grade).submit();
				}
			}
		}
	}

	public static class End extends PvPStageCommon.End {

		@Override
		public void onEnter(PvPControl c) {

			// 结束
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					for (int grade : PvP5Helper.getAllRaceIds()) {
						PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
						if (race.getXRace().getIsend() == 0) {
							PvP5Judge judge = new PvP5Judge(race);
							judge.judge();
							race.end();
						}
					}
					return true;
				}
			}.submit();

			super.onEnter(c);
		}
	}

	public static class Award extends PvPStageCommon.Award {}
}
