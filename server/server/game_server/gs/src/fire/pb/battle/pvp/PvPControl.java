package fire.pb.battle.pvp;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import fire.pb.map.GridPos;
import fire.pb.map.Position;
import fire.pb.map.SceneNpcManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * PvP控制器
 * @author XGM
 */
public abstract class PvPControl {

	public static final Logger logger = Logger.getLogger("PVP");

	// 测试模式
	protected boolean testMode = false;

	public final boolean isTestMode() {
		return testMode;
	}

	public final void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	// PvP当前阶段,注意同步
	protected IPvPStage stage = null;

	// PvP阶段管理器
	protected PvPStageManager stageManager = new PvPStageManager();

	// 活动时间
	protected long startTime = 0;
	protected long endTime = 0;

	// 战斗时间,比活动开启时间要晚
	protected long fightStartTime = 0;
	protected long fightEndTime = 0;

	// 入口NPC
	protected long entryNpcKey = -1;

	// 定时器
	private ScheduledFuture<?> future = null;

	/**
	 * 构造
	 */
	protected PvPControl() {

		// 注册支持项
		registerStage();

		// 默认未开启
		stage = stageManager.get(EPvPStage.NOTOPEN);
	}

	/**
	 * 初始化
	 */
	public void init(long start, long end, long fightStart, long fightEnd) {

		// 时间
		startTime = start;
		endTime = end;
		fightStartTime = fightStart;
		fightEndTime = fightEnd;

		// 进入开始阶段
		changeStage(EPvPStage.START);
	}

	/**
	 * 结束活动
	 */
	public void end() {

		// 重置
		startTime = 0;
		endTime = 0;
		fightStartTime = 0;
		fightEndTime = 0;

		// 结束
		changeStage(EPvPStage.END);
	}

	/**
	 * 关闭(真正结束)
	 */
	public abstract void close();

	/**
	 * 注册PvP支持的阶段
	 */
	protected abstract void registerStage();

	/**
	 * 改变PvP阶段
	 * @param s
	 */
	public final void changeStage(EPvPStage s) {
		//synchronized (stage) { // 切换不会同时执行,不需要锁(因为阶段变化是顺序的)
			IPvPStage oldStage = stage;
			if (oldStage != null) {
				oldStage.onLeave(this);
			}
			IPvPStage newStage = stageManager.get(s);
			if ((stage = newStage) != null) {
				newStage.onEnter(this);
			}
		//}
	}

	/**
	 * 当前是不是活动时间
	 * @return
	 */
	public final boolean isActivityTime() {
		long now = System.currentTimeMillis();
		return isActivityTime(now);
	}

	/**
	 * 是不是活动时间
	 * @return
	 */
	public final boolean isActivityTime(long t) {
		if (t > startTime && t < endTime) {
			return true;
		}
		return false;
	}

	/**
	 * 当前是不是战斗时间
	 * @return
	 */
	public final boolean isFightTime() {
		long now = System.currentTimeMillis();
		if (now > fightStartTime && now < fightEndTime) {
			return true;
		}
		return false;
	}

	/**
	 * 获得战斗开始时间
	 * @return
	 */
	public final long getFightStartTime() {
		return fightStartTime;
	}

	/**
	 * 创建定时器
	 * @param period
	 * @param delay
	 */
	public final void createSchedule(long period, long delay) {

		removeSchedule();

		/**
		 * @see java.util.concurrent.ScheduledThreadPoolExecutor.scheduleAtFixedRate
		 *      throw if (period <= 0) throw new IllegalArgumentException();
		 */
		period = Math.max(1, period); 

		/**
		 * 禁止启动太快!!
		 */
		delay = Math.max(1, delay);

		final IPvPStage theStage = stage;
		future = mkdb.Mkdb.executor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				tick(theStage);
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * 删除定时器
	 */
	public final void removeSchedule() {
		if (future != null) {
			future.cancel(true);
			future = null;
		}
	}

	/**
	 * 心跳
	 * @param theStage
	 */
	public void tick(IPvPStage theStage) {
		if (theStage != null && theStage == stage) {
			theStage.onTick(this);
		}
	}

	/**
	 * 活动NPC配置ID
	 * @return
	 */
	public abstract int getActivityNpcConfigId();

	/**
	 * 创建接引NPC
	 */
	public void createNpc() {

		removeNpc();

		fire.pb.npc.SNpcPos npcPosConf = PvPHelper.getNpcPosByConfigId(getActivityNpcConfigId());
		if (npcPosConf != null) {
			entryNpcKey = SceneNpcManager.getInstance().getNextId();
			Position pos = new GridPos(npcPosConf.getNpcPosx(), npcPosConf.getNpcPosy()).toPosition();
			SceneNpcManager.createNpcByPos(entryNpcKey, npcPosConf.getNpcid(), npcPosConf.getNpcName(),
					npcPosConf.getNpcMap(), pos.getX(), pos.getY(), 3, 0);

			// 加入小地图
//			List<MiniMapNpc> miniMapNpcs = new ArrayList<MiniMapNpc>();
//			miniMapNpcs.add(new MiniMapNpc(npcPosConf.getNpcid(), npcPosConf.getNpcMap(), pos.getX(), pos.getY()));
//			AbstractScheduledActivity.addNpcToMiniMap(miniMapNpcs);

			if (logger.isInfoEnabled()) {
				logger.info("PVP::[PvPControl.createNpc] create npc"
						+ " npcId:" + npcPosConf.getNpcid()
						+ " npcName:" + npcPosConf.getNpcName()
						+ " mapId:" + npcPosConf.getNpcMap()
						+ " posX:" + npcPosConf.getNpcPosx()
						+ " posY:" + npcPosConf.getNpcPosy());
			}
		}
	}

	/**
	 * 删除接引NPC
	 */
	public void removeNpc() {
		if (entryNpcKey > 0) {
			SceneNpcManager.removeNpc(entryNpcKey);
			entryNpcKey = -1;

			if (logger.isInfoEnabled()) {
				logger.info("PVP::[PvPControl.removeNpc] remove npc.");
			}
		}
	}

	/**
	 * @see #doApplyEnter
	 */
	public final boolean applyEnter(final long roleId, int gotoType) {
		return stage.onApplyEnter(this, roleId, gotoType);
	}

	/**
	 * 请求进入赛场
	 * @param roleId
	 * @param gotoType
	 * @return
	 */
	public abstract boolean doApplyEnter(final long roleId, int gotoType);

	/**
	 * @see #doApplyLeave
	 */
	public final void applyLeave(final long roleId) {
		stage.onApplyLeave(this, roleId);
	}

	/**
	 * 请求离开赛场
	 * @param roleId
	 */
	public abstract void doApplyLeave(final long roleId);

	/**
	 * 强制离开赛场
	 * @param roleId
	 */
	public final void forceLeave(final long roleId) {
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (team != null) {
			if (team.isTeamMember(roleId) && team.isNormalMember(roleId)) {
				return;
			}
		}
		transferOutScene(roleId);
	}

	/**
	 * 传出场景
	 * @param roleId
	 */
	public final void transferOutScene(final long roleId) {
		fire.pb.npc.SNpcPos npcPosConf = PvPHelper.getNpcPosByConfigId(getActivityNpcConfigId());
		if (npcPosConf != null) {
			Transfer.justGotoDeltaWhileCommit(roleId, npcPosConf.getNpcMap(),
					npcPosConf.getNpcPosx() + 28, npcPosConf.getNpcPosy() + 28, SRoleEnterScene.CHEFU, 10);
		} else {
			Transfer.justGotoDeltaWhileCommit(roleId, 1615, 141, 131, SRoleEnterScene.CHEFU, 10);
		}
	}

	/**
	 * 角色进入场景
	 * @param roleId
	 * @param mapId
	 * @return
	 */
	public abstract boolean onRoleEnterScene(final long roleId, int mapId);

	/**
	 * 角色离开场景
	 * @param roleId
	 * @param mapId
	 * @param isChangeMap
	 * @return
	 */
	public abstract boolean onRoleLeaveScene(final long roleId, int mapId, boolean isChangeMap);

	/**
	 * 角色上线
	 * @param roleId
	 * @param mapId
	 */
	public abstract void onRoleOnline(final long roleId, int mapId);

	/**
	 * 角色下线
	 * @param roleId
	 */
	public abstract void onRoleOffline(final long roleId);

	/**
	 * @see #doGetAward
	 */
	public final boolean getAward(final long roleId, final int boxType) {
		return stage.onGetAward(this, roleId, boxType);
	}

	/**
	 * 领取奖励
	 * @param roleId
	 * @param boxType
	 * @return
	 */
	public abstract boolean doGetAward(final long roleId, final int boxType);
}
