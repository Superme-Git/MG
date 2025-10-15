package fire.pb.battle.pvp5;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mkdb.Transaction;

import org.apache.log4j.Logger;

import fire.pb.battle.pvp.EPvPStage;
import fire.pb.battle.pvp.PvPControl;
import fire.pb.battle.pvp.PvPResult;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.Transfer;
import fire.pb.talk.MessageMgr;
import fire.pb.util.StringConstant;

/**
 * 5v5的控制
 * @author XGM
 */
public class PvP5Control extends PvPControl {

	static class SingletonHolder {
		static PvP5Control singleton = new PvP5Control();
	}

	public static PvP5Control getInstance() {
		return SingletonHolder.singleton;
	}

	public static Logger getLogger() {
		return logger;
	}

	// 定时奖励的定时器
	private ScheduledFuture<?> futureAward = null;
	public final void createScheduleAward(long period, long delay, Runnable command) {
		// remove
		removeScheduleAward();
		// create
		period = Math.max(1, period); 
		delay = Math.max(1, delay);
		futureAward = mkdb.Mkdb.executor().scheduleAtFixedRate(command,
				delay, period, TimeUnit.SECONDS);
	}
	public final void removeScheduleAward() {
		if (futureAward != null) {
			futureAward.cancel(true);
			futureAward = null;
		}
	}

	@Override
	public void init(long start, long end, long fightStart, long fightEnd) {

		super.init(start, end, fightStart, fightEnd);

		PvP5CampPolicy.init();
		PvP5CampCountdown.getInstance().init();

		PvP5RoleStateManager.getInstance().init();

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP5::[PvP5Control.init] .");
		}
	}

	@Override
	public void end() {

		super.end();

		// 记录日志
		if (logger.isInfoEnabled()) {
			logger.info("PVP5::[PvP5Control.end] .");
		}
	}

	@Override
	public void close() {
		for (int grade : PvP5Helper.getAllRaceIds()) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
					// 访问排行榜数据
					race.acceptVisit(new PvP5RankVisitor());
					// 清空数据
					race.clear();
					return true;
				}
			}.submit();
		}
	}

	@Override
	protected void registerStage() {
		stageManager.register(EPvPStage.NOTOPEN, new PvP5Stage.NotOpen());
		stageManager.register(EPvPStage.START, new PvP5Stage.Start());
		stageManager.register(EPvPStage.PREPARE, new PvP5Stage.Prepare());
		stageManager.register(EPvPStage.FIGHT, new PvP5Stage.Fight());
		stageManager.register(EPvPStage.END, new PvP5Stage.End());
		stageManager.register(EPvPStage.AWARD, new PvP5Stage.Award());
	}

	@Override
	public int getActivityNpcConfigId() {
		return 9;
	}

	@Override
	public boolean doApplyEnter(long roleId, int gotoType) {

		if (isActivityTime() == false) {
			MessageMgr.sendMsgNotify(roleId, 160348, null);
			return false;
		}

		// 如果比赛进行中出去了,可以再进相同的赛场
		PvP5RoleProxy pvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
		if (pvpRoleProxy == null) {
			return false;
		}
		int grade = pvpRoleProxy.getGradeInActivity();
		if (grade <= 0) {
			grade = PvP5Helper.getRaceGradeByRoleId(roleId);
		}
		if (grade <= 0) {
			List<String> param = MessageMgr.getStringList(PvP5Helper.ENTER_MIN_LEVEL, StringConstant.ID2String("5v5竞技场", 294));
			MessageMgr.sendMsgNotify(roleId, 160340, param);
			return false;
		}

		// 如果已经分了胜负,则不能再进
		if (PvP5RaceProxy.isEnd(grade)) {
			MessageMgr.sendMsgNotify(roleId, 160348, null);
			return false;
		}

		// 分阵营
		PvP5CampPolicy.Policy policy = PvP5CampPolicy.create(roleId, grade);
		if (policy == null) {
			return false;
		}
		policy.divide();
		int camp = policy.getCamp();
		int mapId = policy.getMapId();

		// 检测进入条件
		PvPResult result = PvP5Helper.checkRoleEnterCondition(roleId, grade, camp, mapId);
		if (result.get() != 0) {
			result.handle();
			return false;
		}

		// 检测是否已经进入
		Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null) {
			return false;
		}
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

		int grade = PvP5Helper.getRaceGradeByMapId(mapId);
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
				PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
				return race.roleEnter(roleId, mapId);
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

		int grade = PvP5Helper.getRaceGradeByMapId(mapId);
		if (grade <= 0) {
			return false;
		}

		// 重置状态
		PvP5RoleStateManager.getInstance().setRolePvPState(roleId, PvP5RoleStateManager.PVPSTATE_DEFAULT);

		// 离开赛场
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
				return race.roleLeave(roleId);
			}
		};
		if (Transaction.current() != null) {
			if (isChangeMap) {
				proc.call(); // 这里是必须直接调用,因为掉到普通场时,是竞技场间切换,可能会有(race.roleLeave,race.roleEnter)调用的顺序问题 [4/12/2016 XGM]
			} else {
				mkdb.Procedure.pexecuteWhileCommit(proc);
			}
		} else {
			proc.submit();
		}

		return true;
	}

	@Override
	public void onRoleOnline(long roleId, int mapId) {

		// TODO 这里有可能用上吗？有待考证
		if (true) {
			// 上线过程如果被重分了阵营
			int grade = PvP5Helper.getRaceGradeByMapId(mapId);
			PvP5CampPolicy.Policy policy = PvP5CampPolicy.create(roleId, grade);
			if (policy == null) {
				return;
			}
			policy.divide();
			if (mapId != policy.getMapId()) {
				mapId = policy.getMapId();
			}
			// 上线过程如果被重分了阵营
		}

		if (onRoleEnterScene(roleId, mapId) == false) {
			PvP5Helper.summonByRole(roleId, mapId);
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
				PvP5RoleProxy pvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, false);
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
		if (mapRole != null && PvP5Helper.isPvPMap(mapRole.getMapId())) {
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(roleId) == false) {
				PvP5RoleStateManager.getInstance().setRolePvPState(roleId, PvP5RoleStateManager.PVPSTATE_DEFAULT);
			}
		}
	}

}
