package fire.pb.battle.pvp1;

import java.util.List;

import mkdb.Transaction;

import org.apache.log4j.Logger;

import fire.pb.battle.pvp.EPvPStage;
import fire.pb.battle.pvp.PvPControl;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.Transfer;
import fire.pb.talk.MessageMgr;
import fire.pb.util.StringConstant;

/**
 * 1v1的控制
 * @author XGM
 */
public class PvP1Control extends PvPControl {

	static class SingletonHolder {
		static PvP1Control singleton = new PvP1Control();
	}

	public static PvP1Control getInstance() {
		return SingletonHolder.singleton;
	}

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public void init(long start, long end, long fightStart, long fightEnd) {

		super.init(start, end, fightStart, fightEnd);

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP1::[PvP1Control.init] .");
		}
	}

	@Override
	public void end() {

		super.end();

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP1::[PvP1Control.end] .");
		}
	}

	@Override
	public void close() {
		for (int grade : PvP1Helper.getAllRaceIds()) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
					// 清空数据
					race.clear();
					return true;
				}
			}.submit();
		}
	}

	@Override
	protected void registerStage() {
		stageManager.register(EPvPStage.NOTOPEN, new PvP1Stage.NotOpen());
		stageManager.register(EPvPStage.START, new PvP1Stage.Start());
		stageManager.register(EPvPStage.PREPARE, new PvP1Stage.Prepare());
		stageManager.register(EPvPStage.FIGHT, new PvP1Stage.Fight());
		stageManager.register(EPvPStage.END, new PvP1Stage.End());
		stageManager.register(EPvPStage.AWARD, new PvP1Stage.Award());
	}

	@Override
	public int getActivityNpcConfigId() {
		return 7;
	}

	@Override
	public boolean doApplyEnter(long roleId, int gotoType) {

		if (isActivityTime() == false) {
			MessageMgr.sendMsgNotify(roleId, 160348, null);
			return false;
		}

		// 如果比赛进行中出去了,可以再进相同的赛场
		PvP1RoleProxy pvpRoleProxy = PvP1RoleProxy.getPvP1RoleProxy(roleId, true);
		if (pvpRoleProxy == null) {
			return false;
		}
		int grade = pvpRoleProxy.getGradeInActivity();
		if (grade <= 0) {
			grade = PvP1Helper.getRaceGradeByRoleId(roleId);
		}
		if (grade <= 0) {
			List<String> param = MessageMgr.getStringList(PvP1Helper.ENTER_MIN_LEVEL, StringConstant.ID2String("竞技场", 292));
			MessageMgr.sendMsgNotify(roleId, 160340, param);
			return false;
		}

		// 检测进入条件
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_TEAM)) {
			// 组队中不能进
			MessageMgr.sendMsgNotify(roleId, 160345, null);
			return false;
		}

		// 检测是否已经进入
		Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null) {
			return false;
		}
		int mapId = PvP1Helper.getMapIdByRaceGrade(grade);
		if (mapRole.getMapId() == mapId) {
			onRoleEnterScene(roleId, mapId);
			return true;
		}

		MapConfig mapConf = ConfigManager.getInstance().getConf(MapConfig.class).get(mapId);
		boolean ret = Transfer.justGotoDeltaWhileCommit(roleId, mapId, mapConf.getXjPos(), mapConf.getYjPos(), gotoType, 10);
		return ret;
	}

	@Override
	public void doApplyLeave(long roleId) {
		transferOutScene(roleId);
	}

	@Override
	public boolean onRoleEnterScene(long roleId, int mapId) {

		int grade = PvP1Helper.getRaceGradeByMapId(mapId);
		if (grade <= 0) {
			return false;
		}

		// 活动结束时离开场景
		if (isActivityTime() == false) {
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER) == false
					&& fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_WATCHER) == false
					&& fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_REPLAY) == false) {
				forceLeave(roleId);
			}
			return true;
		}

		// 退出组队自动匹配
		if (Transaction.current() != null) {
			fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleId);
		} else {
			new mkdb.Procedure() {
				@Override
				public boolean process() {
					fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleId);
					return true;
				}
			}.submit();
		}

		// 进入赛场
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
				return race.roleEnter(roleId);
			}
		};
		if (Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(proc);
		} else {
			proc.submit();
		}

		return true;
	}

	@Override
	public boolean onRoleLeaveScene(long roleId, int mapId, boolean isChangeMap) {

		int grade = PvP1Helper.getRaceGradeByMapId(mapId);
		if (grade <= 0) {
			return false;
		}

		// 离开赛场
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
				return race.roleLeave(roleId);
			}
		};
		if (Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(proc);
		} else {
			proc.submit();
		}

		return true;
	}

	@Override
	public void onRoleOnline(long roleId, int mapId) {
		if (onRoleEnterScene(roleId, mapId) == false) {
			PvP1Helper.summonByRole(roleId, mapId);
		}
	}

	@Override
	public void onRoleOffline(long roleId) {
		int mapId = xtable.Properties.selectSceneid(roleId).intValue();
		onRoleLeaveScene(roleId, mapId, false);
	}

	@Override
	public boolean doGetAward(final long roleId, final int boxType) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP1RoleProxy pvpRoleProxy = PvP1RoleProxy.getPvP1RoleProxy(roleId, false);
				return pvpRoleProxy.openBox(boxType);
			}
		}.submit();
		return true;
	}

}
