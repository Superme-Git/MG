package fire.pb.battle.pvp1;

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
 * 1v1各个阶段的实现
 * @author XGM
 */
public class PvP1Stage {

	public static class NotOpen extends PvPStageCommon.NotOpen {}

	public static class Start extends PvPStageCommon.Start {

		@Override
		public void onEnter(PvPControl c) {

			// 启动
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					for (int grade : PvP1Helper.getAllRaceIds()) {
						PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
						race.begin();
					}
					return true;
				}
			}.submit();

			// 拉人
			RoleManager.getRolesByConditions(-1, PvP1Helper.ENTER_MIN_LEVEL,
					-1, -1, -1, new IGetRolesCallBack() {
						@Override
						public void process(List<Long> roleIds) {
							SGeneralSummonCommand send = new SGeneralSummonCommand();
							send.roleid = 0;
							send.mapid = 0;
							send.minimal = 0;
							send.summontype = TransmitTypes.singlepvp;
							mkdb.Procedure.psendWhileCommit(roleIds, send);
						}
					});

			// 通知客户端活动开启
			SActivityOpen so = new SActivityOpen();
			so.activityid = 240;
			SceneManager.sendAll(so);

			super.onEnter(c);
		}
	}

	public static class Prepare extends PvPStageCommon.Prepare {}

	public static class Fight extends PvPStageCommon.Fight {

		@Override
		public void onTick(PvPControl c) {
			if (c.isFightTime()) {
				// 进行匹配
				for (int grade : PvP1Helper.getAllRaceIds()) {
					new PPvP1PairBattleFromQueue(grade).submit();
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
					for (int grade : PvP1Helper.getAllRaceIds()) {
						PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
						race.end();
					}
					return true;
				}
			}.submit();

			super.onEnter(c);
		}
	}

	public static class Award extends PvPStageCommon.Award {}
}
