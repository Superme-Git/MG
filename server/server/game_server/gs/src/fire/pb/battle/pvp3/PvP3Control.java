package fire.pb.battle.pvp3;

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
 * 3v3的控制
 * @author XGM
 */
public class PvP3Control extends PvPControl {

	static class SingletonHolder {
		static PvP3Control singleton = new PvP3Control();
	}

	public static PvP3Control getInstance() {
		return SingletonHolder.singleton;
	}

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public void init(long start, long end, long fightStart, long fightEnd) {

		super.init(start, end, fightStart, fightEnd);

		PvP3RoleStateManager.getInstance().init();

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP3::[PvP3Control.init] .");
		}
	}

	@Override
	public void end() {

		super.end();

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP3::[PvP3Control.end] .");
		}
	}

	@Override
	public void close() {
		for (int grade : PvP3Helper.getAllRaceIds()) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
					// 清空数据
					race.clear();
					return true;
				}
			}.submit();
		}
	}

	@Override
	protected void registerStage() {
		stageManager.register(EPvPStage.NOTOPEN, new PvP3Stage.NotOpen());
		stageManager.register(EPvPStage.START, new PvP3Stage.Start());
		stageManager.register(EPvPStage.PREPARE, new PvP3Stage.Prepare());
		stageManager.register(EPvPStage.FIGHT, new PvP3Stage.Fight());
		stageManager.register(EPvPStage.END, new PvP3Stage.End());
		stageManager.register(EPvPStage.AWARD, new PvP3Stage.Award());
	}

	@Override
	public int getActivityNpcConfigId() {
		return 8;
	}

	@Override
	public boolean doApplyEnter(final long roleId, int gotoType) {

		if (isActivityTime() == false) {
			MessageMgr.sendMsgNotify(roleId, 160348, null);
			return false;
		}

		// 如果比赛进行中出去了,可以再进相同的赛场
		PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(roleId, true);
		if (pvpRoleProxy == null) {
			return false;
		}
		int grade = pvpRoleProxy.getGradeInActivity();
		if (grade <= 0) {
			grade = PvP3Helper.getRaceGradeByRoleId(roleId);
		}
		if (grade <= 0) {
			List<String> param = MessageMgr.getStringList(PvP3Helper.ENTER_MIN_LEVEL, StringConstant.ID2String("3v3竞技场", 293));
			MessageMgr.sendMsgNotify(roleId, 160340, param);
			return false;
		}

		// 检测进入条件
		int error = PvP3Helper.checkRoleEnterCondition(roleId, grade);
		if (error != 0) {
			switch (error) {
			case -1: // 只有队长才能操作
				MessageMgr.sendMsgNotify(roleId, 141206, null);
				break;
			case -2: // 队伍人数超出3人
				MessageMgr.sendMsgNotify(roleId, 160337, null);
				break;
			case -4: // 队员等级不匹配
			default:
				MessageMgr.sendMsgNotify(roleId, 160339, null);
				break;
			}
			return false;
		}

		// 检测是否已经进入
		Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null) {
			return false;
		}
		int mapId = PvP3Helper.getMapIdByRaceGrade(grade);
		if (mapId == mapRole.getMapId()) {
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

		int grade = PvP3Helper.getRaceGradeByMapId(mapId);
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
				PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
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

		int grade = PvP3Helper.getRaceGradeByMapId(mapId);
		if (grade <= 0) {
			return false;
		}

		// 重置状态
		PvP3RoleStateManager.getInstance().setRolePvPState(roleId, PvP3RoleStateManager.PVPSTATE_DEFAULT);

		// 离开赛场
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
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
			PvP3Helper.summonByRole(roleId, mapId);
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
				PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(roleId, false);
				return pvpRoleProxy.openBox(boxType);
			}
		}.submit();
		return true;
	}

	/**
	 * 响应队伍成员离开
	 * @param roleId
	 */
	public void onTeamMemberLeave(long roleId) {
		final fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole != null && PvP3Helper.isPvPMap(mapRole.getMapId())) {
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(roleId) == false) {
				mkdb.Procedure.psendWhileCommit(roleId, new SPvP3ReadyFight((byte) 0));
				PvP3RoleStateManager.getInstance().setRolePvPState(roleId, PvP3RoleStateManager.PVPSTATE_DEFAULT);
			}
		}
	}

}
