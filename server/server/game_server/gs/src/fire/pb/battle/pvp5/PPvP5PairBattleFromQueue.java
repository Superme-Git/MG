package fire.pb.battle.pvp5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import fire.pb.circletask.PVPMatchTimeConfig;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

public class PPvP5PairBattleFromQueue extends Procedure {

	private final int grade;

	public Map<Integer,PVPMatchTimeConfig> matchConfs = ConfigManager.getInstance().getConf(PVPMatchTimeConfig.class);

	public PPvP5PairBattleFromQueue(int grade) {
		this.grade = grade;
	}

	@Override
	protected boolean process() {

		PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
		if (race.getXRace() == null) {
			return false;
		}
		if (race.isOpening() == false) {
			return false;
		}
		if (race.getXRace().getWaitingqueue().size() < 2) {
			return false; // 队列人数不够
		}

		long now = System.currentTimeMillis();
		List<PvP5RoleProxy> pvpRoleProxys = new ArrayList<PvP5RoleProxy>();
		for (Iterator<xbean.PvP5QueueRole> it = race.getXRace().getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP5QueueRole queueRole = it.next();
			PvP5RoleProxy pvpRoleProxy = getValidRole(race, queueRole.getRoleid(), now);
			if (pvpRoleProxy == null) {
				it.remove(); // 移除不合法的
				// 关闭匹配状态
				PvP5RoleProxy.setTeamMemberPvPState(queueRole.getRoleid(), PvP5RoleStateManager.PVPSTATE_DEFAULT);
				// 你的队伍无法进行战斗，请检查队伍成员
				PvP5RoleProxy.notifyTeamProtocol(queueRole.getRoleid(), MessageMgr.getMsgNotify(172026, 0, null));
			} else {
				pvpRoleProxys.add(pvpRoleProxy);
			}
		}

		int queueSize = pvpRoleProxys.size();
		int pairNum = selectPair(race, pvpRoleProxys, 0, 0, now);
		if (PvP5Control.getLogger().isInfoEnabled()) {
			PvP5Control.getLogger().info("PVP5::[PPvP5PairBattleFromQueue] pair end."
					+ " grade:" + grade
					+ " queueSize:" + queueSize
					+ " pairNum:" + pairNum
					+ " roleNumCampA:" + race.getXRace().getAllrolesidcampa().size()
					+ " roleNumCampB:" + race.getXRace().getAllrolesidcampb().size()
					+ " rankNumCampA:" + race.getXRace().getAllrolescampa().size()
					+ " rankNumCampB:" + race.getXRace().getAllrolescampb().size());
		}
		return true;
	}

	/**
	 * 获取有效的角色
	 * @param race
	 * @param roleId
	 * @param now
	 * @return
	 */
	private static PvP5RoleProxy getValidRole(PvP5RaceProxy race, long roleId, long now) {

		PvP5RoleProxy pvpRoleProxy = null;
		List<Long> roleIds = PvP5RoleProxy.getTeamMemberIds(roleId);
		if (roleIds.get(0) == roleId) { // roleId必须是队长
			for (long id : roleIds) {
				PvP5RoleProxy tempPvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(id, true);
				if (race.roleIsCanReady(tempPvpRoleProxy.getRoleId(), false) == false)
					return null;
				if (tempPvpRoleProxy.getRoleId() == roleId) {
					pvpRoleProxy = tempPvpRoleProxy;
				}
			}
		}
		return pvpRoleProxy;
	}

	/**
	 * 返回这次匹配成对的数量
	 * @param race
	 * @param pvpRoleProxy
	 * @param startIndex
	 * @param pairNum
	 * @param now
	 * @return
	 */
	private int selectPair(PvP5RaceProxy race, List<PvP5RoleProxy> pvpRoleProxys, int startIndex, int pairNum, long now) {

		int size = race.getXRace().getWaitingqueue().size();
		if (startIndex >= size - 1)
			return pairNum; // 结束

		xbean.PvP5QueueRole hostRole = race.getXRace().getWaitingqueue().get(startIndex);
		PvP5RoleProxy pvpHostRole = pvpRoleProxys.get(startIndex);

		long hostWaitingTime = Math.max(0, now - hostRole.getEnterqueuetime() - PvP5Helper.MATCH_WAITING_MILLISECONDS);
		if (roleCanFight(pvpHostRole, now, hostWaitingTime) == false) {
			return selectPair(race, pvpRoleProxys, startIndex + 1, pairNum, now); // 失败,下一个
		}

		for (int i = startIndex + 1; i < size; i++)
		{
			PvP5RoleProxy pvpGuestRole = pvpRoleProxys.get(i);
			long guestWaitingTime = Math.max(0, now - hostRole.getEnterqueuetime() - PvP5Helper.MATCH_WAITING_MILLISECONDS);
			if (roleCanFight(pvpGuestRole, now, guestWaitingTime) == false)
				continue;

			// 配对
			if (pair(pvpHostRole, pvpGuestRole, hostWaitingTime) == false)
				continue;

			race.getXRace().getWaitingqueue().remove(startIndex);
			pvpRoleProxys.remove(startIndex);

			race.getXRace().getWaitingqueue().remove(i - 1);
			pvpRoleProxys.remove(i - 1);

			sendMatchResult(pvpHostRole.getRoleId(), pvpGuestRole.getRoleId());
			sendMatchResult(pvpGuestRole.getRoleId(), pvpHostRole.getRoleId());

			// 创建战斗
			NewBattle(grade, pvpHostRole.getRoleId(), pvpGuestRole.getRoleId(), now);

			return selectPair(race, pvpRoleProxys, startIndex, pairNum+1, now); // 成功配对
		}

		return selectPair(race, pvpRoleProxys, startIndex + 1, pairNum, now); // 失败,下一个
	}

	private boolean pair(PvP5RoleProxy pvpHostRole, PvP5RoleProxy pvpGuestRole, long waitingTime) {

		if (pvpHostRole.getRoleId() == pvpGuestRole.getRoleId())
			return false; // 自己跟自己？不可能出现的情况

//		if (pvpHostRole.getLastEnemyId() == pvpGuestRole.getRoleId()
//				|| pvpHostRole.getRoleId() == pvpGuestRole.getLastEnemyId()) {
//			// return false;
//		}

//		if (pvpHostRole.getLastFightRoleIds().contains(pvpGuestRole.getRoleId())) {
//			return false;
//		}

		if (pvpHostRole.getCamp() == pvpGuestRole.getCamp()) {
			return false;
		}

		if (pvpHostRole.getExcellent() != pvpGuestRole.getExcellent()) {
			return false;
		}

		int hostLevel = pvpHostRole.getScoreLevel();
		int guestLevel = pvpGuestRole.getScoreLevel();
		int seconds = (int) (waitingTime / 1000) + 1;

		int levelmod = 999;// 最大等级差暂定999
		PVPMatchTimeConfig conf = matchConfs.get(seconds);
		if (conf != null) {
			levelmod = conf.lev;
		}
		int hostRoleLv = xtable.Properties.selectLevel(pvpHostRole.getRoleId());
		int guestRoleLv = xtable.Properties.selectLevel(pvpGuestRole.getRoleId()); // 额外等级差
		return (Math.abs(hostLevel - guestLevel) + Math.abs(hostRoleLv - guestRoleLv) / 5) <= levelmod;
	}
	
	/**
	 * 能否进入战斗
	 * @param pvpRoleProxy
	 * @param now
	 * @return
	 */
	public static boolean roleCanFight(PvP5RoleProxy pvpRoleProxy, long now, long waitingTime) {

		// 检测战斗间隔
		long t = now - pvpRoleProxy.getLastBattleTime();
		long t2 = now - pvpRoleProxy.getLastEnterTime();
		if (t > PvP5Helper.MATCH_WAITING_MILLISECONDS && t2 > PvP5Helper.MATCH_WAITING_MILLISECONDS) {
			return true;
		}
		return false;
	}

	/**
	 * 通知匹配的结果
	 * @param roleId
	 * @param targetId
	 */
	public static void sendMatchResult(long roleId, long targetId) {

		// 获取目标所在队伍的所有成员
		//List<Long> targetRoleIds = PvP5RoleProxy.getTeamMemberIds(targetId);

		// 发送匹配结果
		SPvP5MatchResult send = new SPvP5MatchResult();

		// 发送给自己队伍
		PvP5RoleProxy.notifyTeamProtocol(roleId, send);
	}

	/**
	 * 创建战斗
	 * @param grade
	 * @param hostRoleId
	 * @param guestRoleId
	 * @param now
	 */
	private static void NewBattle(final int grade, final long hostRoleId, final long guestRoleId, final long now) {
		final List<Long> hostTeamMemberIds = PvP5RoleProxy.getTeamMemberIds(hostRoleId);
		final List<Long> guestTeamMemberIds = PvP5RoleProxy.getTeamMemberIds(guestRoleId);
		// 设置准备战斗中状态
		PvP5RoleProxy.setTeamMemberPvPState(hostRoleId, PvP5RoleStateManager.PVPSTATE_BATTLE_PREPARE);
		PvP5RoleProxy.setTeamMemberPvPState(guestRoleId, PvP5RoleStateManager.PVPSTATE_BATTLE_PREPARE);
		// 1秒后开始战斗
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			public boolean process() {
				mkdb.Mkdb.executor().schedule(new Runnable() {
					@Override
					public void run() {
						new mkdb.Procedure() {
							@Override
							public boolean process() {

								boolean valid = true;
								PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
								if (race.getXRace() == null) {
									valid = false;
								}
								Set<Long> roleIdSet = new HashSet<Long>();
								// 检查队员变化
								if (valid) {
									List<Long> roleIds = PvP5RoleProxy.getTeamMemberIds(hostRoleId);
									if (roleIds.size() > PvP5Helper.TEAM_MAX_MEMBER_NUM
											|| roleIds.size() != hostTeamMemberIds.size()
											|| roleIds.containsAll(hostTeamMemberIds) == false) {
										valid = false;
									}
									roleIdSet.addAll(roleIds);
								}
								if (valid) {
									List<Long> roleIds = PvP5RoleProxy.getTeamMemberIds(guestRoleId);
									if (roleIds.size() > PvP5Helper.TEAM_MAX_MEMBER_NUM
											|| roleIds.size() != guestTeamMemberIds.size()
											|| roleIds.containsAll(guestTeamMemberIds) == false) {
										valid = false;
									}

									// 队员重复的问题:A匹配,B邀请A入队,B匹配,A点击确认邀请,会造成自己打自己
									for (long id : roleIds) {
										if (roleIdSet.contains(id)) {
											valid = false;
											break;
										}
									}
								}
								// 检查成员有效性
								if (valid) {
									if (getValidRole(race, hostRoleId, now) == null) {
										valid = false;
									}
								}
								if (valid) {
									if (getValidRole(race, hostRoleId, now) == null) {
										valid = false;
									}
								}

								if (valid) {
									// 进入战斗
									long hostTeamId = -1;
									long guestTeamId = -1;
									Long tempHostTeamId = xtable.Roleid2teamid.select(hostRoleId);
									if (tempHostTeamId != null) {
										hostTeamId = tempHostTeamId;
									}
									Long tempGuestTeamId = xtable.Roleid2teamid.select(guestRoleId);
									if (tempGuestTeamId != null) {
										guestTeamId = tempGuestTeamId;
									}
									mkdb.Procedure.pexecuteWhileCommit(new PPvP5NewBattle(grade, hostRoleId, guestRoleId, hostTeamId, guestTeamId));
								} else {
									// 客户端关闭倒计时界面
									// ...
									// 关闭匹配状态
									PvP5RoleProxy.setTeamMemberPvPState(hostRoleId, PvP5RoleStateManager.PVPSTATE_DEFAULT);
									PvP5RoleProxy.setTeamMemberPvPState(guestRoleId, PvP5RoleStateManager.PVPSTATE_DEFAULT);
								}
								return true;
							}
						}.submit();
					}
				}, 1, TimeUnit.SECONDS);
				return true;
			}
		});
	}
}
