package fire.pb.battle.pvp3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import fire.pb.PropRole;
import fire.pb.circletask.PVPMatchTimeConfig;
import fire.pb.main.ConfigManager;

public class PPvP3PairBattleFromQueue extends Procedure {

	private final int grade;

	public Map<Integer,PVPMatchTimeConfig> matchConfs = ConfigManager.getInstance().getConf(PVPMatchTimeConfig.class);

	public PPvP3PairBattleFromQueue(int grade) {
		this.grade = grade;
	}

	@Override
	protected boolean process() {

		PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
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
		List<PvP3RoleProxy> pvpRoleProxys = new ArrayList<PvP3RoleProxy>();
		for (Iterator<xbean.PvP3QueueRole> it = race.getXRace().getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP3QueueRole queueRole = it.next();
			PvP3RoleProxy pvpRoleProxy = getValidRole(race, queueRole.getRoleid(), now);
			if (pvpRoleProxy == null) {
				it.remove(); // 移除不合法的
				// 关闭匹配状态
				PvP3RoleProxy.setTeamMemberPvPState(queueRole.getRoleid(), PvP3RoleStateManager.PVPSTATE_DEFAULT);
				// 通知客户端
				PvP3RoleProxy.notifyTeamProtocol(queueRole.getRoleid(), new SPvP3ReadyFight((byte)0));
			} else {
				pvpRoleProxys.add(pvpRoleProxy);
			}
		}

		int queueSize = pvpRoleProxys.size();
		int pairNum = selectPair(race, pvpRoleProxys, 0, 0, now);
		if (PvP3Control.getLogger().isInfoEnabled()) {
			PvP3Control.getLogger().info("PVP3::[PPvP3PairBattleFromQueue] pair end."
					+ " grade:" + grade
					+ " queueSize:" + queueSize
					+ " pairNum:" + pairNum
					+ " roleNum:" + race.getXRace().getAllrolesid().size()
					+ " rankNum:" + race.getXRace().getAllroles().size());
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
	private static PvP3RoleProxy getValidRole(PvP3RaceProxy race, long roleId, long now) {

		PvP3RoleProxy pvpRoleProxy = null;
		List<Long> roleIds = PvP3RoleProxy.getTeamMemberIds(roleId);
		for (long id : roleIds) {
			PvP3RoleProxy tempPvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(id, true);
			if(race.roleIsCanReady(tempPvpRoleProxy.getRoleId(), false) == false)
				return null;
			if (tempPvpRoleProxy.getRoleId() == roleId) {
				pvpRoleProxy = tempPvpRoleProxy;
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
	private int selectPair(PvP3RaceProxy race, List<PvP3RoleProxy> pvpRoleProxys, int startIndex, int pairNum, long now) {

		int size = race.getXRace().getWaitingqueue().size();
		if(startIndex >= size - 1)
			return pairNum; //结束

		xbean.PvP3QueueRole hostRole = race.getXRace().getWaitingqueue().get(startIndex);
		PvP3RoleProxy pvpHostRole = pvpRoleProxys.get(startIndex);
		if(roleCanFight(pvpHostRole, now) == false) {
			return selectPair(race, pvpRoleProxys, startIndex + 1, pairNum, now); // 失败,下一个
		}

		long waitingTime = Math.max(0, now - hostRole.getEnterqueuetime());
		for(int i = startIndex + 1; i < size; i++)
		{
			PvP3RoleProxy pvpGuestRole = pvpRoleProxys.get(i);
			if (roleCanFight(pvpGuestRole, now) == false)
				continue;

			// 配对
			if (pair(pvpHostRole, pvpGuestRole, waitingTime) == false)
				continue;

			race.getXRace().getWaitingqueue().remove(startIndex);
			pvpRoleProxys.remove(startIndex);
			//PvP3RoleProxy.notifyTeamProtocol(pvpHostRole.getRoleId(), new SPvP3ReadyFight((byte)0));

			race.getXRace().getWaitingqueue().remove(i - 1);
			pvpRoleProxys.remove(i - 1);
			//PvP3RoleProxy.notifyTeamProtocol(pvpGuestRole.getRoleId(), new SPvP3ReadyFight((byte)0));

			sendMatchResult(pvpHostRole.getRoleId(), pvpGuestRole.getRoleId());
			sendMatchResult(pvpGuestRole.getRoleId(), pvpHostRole.getRoleId());

			// 创建战斗
			NewBattle(grade, pvpHostRole.getRoleId(), pvpGuestRole.getRoleId(), now);

			return selectPair(race, pvpRoleProxys, startIndex, pairNum+1, now); // 成功配对
		}

		return selectPair(race, pvpRoleProxys, startIndex + 1, pairNum, now); // 失败,下一个
	}

	private boolean pair(PvP3RoleProxy pvpHostRole, PvP3RoleProxy pvpGuestRole, long waitingTime) {

		if (pvpHostRole.getRoleId() == pvpGuestRole.getRoleId())
			return false; // 自己跟自己？不可能出现的情况
/*
		if (pvpHostRole.getLastEnemyId() == pvpGuestRole.getRoleId()
				|| pvpHostRole.getRoleId() == pvpGuestRole.getLastEnemyId()) {
			// return false;
		}
*/
//		if (pvpHostRole.getLastFightRoleIds().contains(pvpGuestRole.getRoleId())) {
//			return false;
//		}

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
	public static boolean roleCanFight(PvP3RoleProxy pvpRoleProxy, long now){

		// 检测战斗间隔
		if(now - pvpRoleProxy.getLastBattleTime() < PvP3Helper.BATTLE_BETWEEN_MILLISECONDS){
			return false;
		}
		return true;
	}

	/**
	 * 通知匹配的结果
	 * @param roleId
	 * @param targetId
	 */
	public static void sendMatchResult(long roleId, long targetId) {

		// 获取目标所在队伍的所有成员
		List<Long> targetRoleIds = PvP3RoleProxy.getTeamMemberIds(targetId);

		// 发送匹配结果
		SPvP3MatchResult send = new SPvP3MatchResult();
		for (long id : targetRoleIds) {

			// 目标属性
			PropRole targetAttr = new PropRole(id, true);

			fire.pb.battle.pvp3.PvP3RoleSingleMatch targetInfo = new fire.pb.battle.pvp3.PvP3RoleSingleMatch();
			targetInfo.roleid = id;
			targetInfo.level = (short)targetAttr.getLevel();
			targetInfo.shape = targetAttr.getShape();
			targetInfo.school = targetAttr.getSchool();
			send.targets.add(targetInfo);
		}

		// 发送给自己队伍
		PvP3RoleProxy.notifyTeamProtocol(roleId, send);
	}

	/**
	 * 创建战斗
	 * @param grade
	 * @param hostRoleId
	 * @param guestRoleId
	 * @param now
	 */
	private static void NewBattle(final int grade, final long hostRoleId, final long guestRoleId, final long now) {
		final List<Long> hostTeamMemberIds = PvP3RoleProxy.getTeamMemberIds(hostRoleId);
		final List<Long> guestTeamMemberIds = PvP3RoleProxy.getTeamMemberIds(guestRoleId);
		// 设置准备战斗中状态
		PvP3RoleProxy.setTeamMemberPvPState(hostRoleId, PvP3RoleStateManager.PVPSTATE_BATTLE_PREPARE);
		PvP3RoleProxy.setTeamMemberPvPState(guestRoleId, PvP3RoleStateManager.PVPSTATE_BATTLE_PREPARE);
		// 3秒后开始战斗
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
								PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
								if (race.getXRace() == null) {
									valid = false;
								}
								Set<Long> roleIdSet = new HashSet<Long>();
								// 检查队员变化
								if (valid) {
									List<Long> roleIds = PvP3RoleProxy.getTeamMemberIds(hostRoleId);
									if (roleIds.size() > PvP3Helper.TEAM_MAX_MEMBER_NUM
											|| roleIds.size() != hostTeamMemberIds.size()
											|| roleIds.containsAll(hostTeamMemberIds) == false) {
										valid = false;
									}
									roleIdSet.addAll(roleIds);
								}
								if (valid) {
									List<Long> roleIds = PvP3RoleProxy.getTeamMemberIds(guestRoleId);
									if (roleIds.size() > PvP3Helper.TEAM_MAX_MEMBER_NUM
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
									mkdb.Procedure.pexecuteWhileCommit(new PPvP3NewBattle(grade, hostRoleId, guestRoleId, hostTeamId, guestTeamId));
								} else {
									// 客户端关闭倒计时界面
									PvP3RoleProxy.notifyTeamProtocol(hostRoleId, new SPvP3ReadyFight((byte) 0));
									PvP3RoleProxy.notifyTeamProtocol(guestRoleId, new SPvP3ReadyFight((byte) 0));
									// 关闭匹配状态
									PvP3RoleProxy.setTeamMemberPvPState(hostRoleId, PvP3RoleStateManager.PVPSTATE_DEFAULT);
									PvP3RoleProxy.setTeamMemberPvPState(guestRoleId, PvP3RoleStateManager.PVPSTATE_DEFAULT);
								}
								return true;
							}
						}.submit();
					}
				}, 3, TimeUnit.SECONDS);
				return true;
			}
		});
	}
}
