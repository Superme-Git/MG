package fire.pb.battle.pvp1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.pb.PropRole;
import fire.pb.circletask.PVPMatchTimeConfig;
import fire.pb.main.ConfigManager;

public class PPvP1PairBattleFromQueue extends mkdb.Procedure {

	private final int grade;

	public Map<Integer, PVPMatchTimeConfig> matchcfgs = ConfigManager.getInstance().getConf(PVPMatchTimeConfig.class);

	public PPvP1PairBattleFromQueue(int grade) {
		this.grade = grade;
	}

	@Override
	protected boolean process() throws Exception {

		PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
		if (race.getXRace() == null) {
			return false;
		}
		if (race.isOpening() == false) {
			return false;
		}
		if (race.getXRace().getWaitingqueue().size() < 2)
			return false;// 队列人数不够

		long now = System.currentTimeMillis();
		List<PvP1RoleProxy> pvpRoles = new ArrayList<PvP1RoleProxy>();
		for (Iterator<xbean.PvP1QueueRole> it = race.getXRace().getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP1QueueRole queueRole = it.next();
			PvP1RoleProxy pvpRole = validRole(race, queueRole.getRoleid(), now);
			if (pvpRole == null) {
				it.remove();// 移除不合法的
				mkdb.Procedure.psendWhileCommit(queueRole.getRoleid(), new SPvP1ReadyFight((byte) 0));
			} else {
				pvpRoles.add(pvpRole);
			}
		}

		int queueSize = pvpRoles.size();
		int pairNum = selectPair(race, pvpRoles, 0, 0, now);
		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[PPvP1PairBattleFromQueue] pair end."
					+ " grade:" + grade
					+ " queueSize:" + queueSize
					+ " pairNum:" + pairNum
					+ " roleNum:" + race.getXRace().getAllrolesid().size()
					+ " rankNum:" + race.getXRace().getAllroles().size());
		}
		return true;
	}

	/**
	 * 检查角色能否进入战斗
	 * @param race
	 * @param roleId
	 * @param now
	 * @return
	 */
	private static PvP1RoleProxy validRole(PvP1RaceProxy race, final long roleId, long now) {

		PvP1RoleProxy pvpRole = PvP1RoleProxy.getPvP1RoleProxy(roleId, true);
		if (race.roleIsCanReady(roleId) == false)
			return null;
		return pvpRole;
	}

	/**
	 * 返回这次匹配成对的数量
	 * @param race
	 * @param pvpRoles
	 * @param startIndex
	 * @param pairnum
	 * @param now
	 * @return
	 */
	private int selectPair(PvP1RaceProxy race, List<PvP1RoleProxy> pvpRoles, int startIndex, int pairnum, long now) {

		int size = race.getXRace().getWaitingqueue().size();
		if (startIndex >= size - 1)
			return pairnum;

		xbean.PvP1QueueRole hostRole = race.getXRace().getWaitingqueue().get(startIndex);
		PvP1RoleProxy pvpHostRole = pvpRoles.get(startIndex);
		if (roleCanFight(pvpHostRole, now) == false)
			return selectPair(race, pvpRoles, startIndex + 1, pairnum, now); // 失败,下一个

		long waitingTime = Math.max(0, now - hostRole.getEnterqueuetime());
		for (int i = startIndex + 1; i < size; i++) {

			PvP1RoleProxy pvpGuestRole = pvpRoles.get(i);
			if (roleCanFight(pvpGuestRole, now) == false)
				continue;

			// 配对
			if (pair(pvpHostRole, pvpGuestRole, waitingTime) == false)
				continue;

			race.getXRace().getWaitingqueue().remove(startIndex);
			pvpRoles.remove(startIndex);
			//xdb.Procedure.psendWhileCommit(pvphostrole.roleId, new SReadyFight((byte) 0));

			race.getXRace().getWaitingqueue().remove(i - 1);
			pvpRoles.remove(i - 1);
			//xdb.Procedure.psendWhileCommit(pvpguestrole.roleId, new SReadyFight((byte) 0));

			sendMatchResult(pvpHostRole.getRoleId(), pvpGuestRole.getRoleId());
			sendMatchResult(pvpGuestRole.getRoleId(), pvpHostRole.getRoleId());

			// xdb.Procedure.pexecuteWhileCommit(new PNewSPvPBattle(pvphostrole.roleId, pvpguestrole.roleId, grade));

			// 创建战斗
			NewBattle(grade, pvpHostRole.getRoleId(), pvpGuestRole.getRoleId(), now);

			return selectPair(race, pvpRoles, startIndex, pairnum + 1, now); // 成功配对
		}

		return selectPair(race, pvpRoles, startIndex + 1, pairnum, now); // 失败,下一个
	}

	private boolean pair(PvP1RoleProxy pvpHostRole, PvP1RoleProxy pvpGuestRole, long waitingTime) {

		if (pvpHostRole.getRoleId() == pvpGuestRole.getRoleId())
			return false; // 自己跟自己？不可能出现的情况
/*
		if (pvphostrole.getLastEnermyId() == pvpguestrole.roleId
				|| pvphostrole.roleId == pvpguestrole.getLastEnermyId())
			return false;
*/
/*
		if (pvpHostRole.getLastfightroleids().contains(pvpGuestRole.roleId)) {
			// return false;
		}
*/
		int hostlevel = pvpHostRole.getScoreLevel();
		int guestlevel = pvpGuestRole.getScoreLevel();
		int seconds = (int) (waitingTime / 1000) + 1;

		int levelmod = 999;// 最大等级差暂定999
		PVPMatchTimeConfig cfg = matchcfgs.get(seconds);
		if (cfg != null)
			levelmod = cfg.lev;
		int hostrolelv = xtable.Properties.selectLevel(pvpHostRole.getRoleId());
		int guestrolelv = xtable.Properties.selectLevel(pvpGuestRole.getRoleId());// 额外等级差
		return (Math.abs(hostlevel - guestlevel) + Math.abs(hostrolelv - guestrolelv) / 5) <= levelmod;
	}

	/**
	 * 能否进入战斗
	 * @param pvpRole
	 * @param now
	 * @return
	 */
	public boolean roleCanFight(PvP1RoleProxy pvpRole, long now) {

		// 检测战斗间隔
		if (now - pvpRole.getLastBattleTime() < PvP1Helper.BATTLE_BETWEEN_MILLISECONDS) {
			return false;
		}
		return true;
	}

	/**
	 * 通知匹配的结果
	 * @param roleId
	 * @param targetId
	 */
	public void sendMatchResult(long roleId, long targetId) {

		// 目标属性
		PropRole targetAttr = new PropRole(targetId, true);

		// 发送匹配结果
		fire.pb.battle.pvp1.PvP1RoleSingleMatch targetInfo = new fire.pb.battle.pvp1.PvP1RoleSingleMatch();
		targetInfo.roleid = targetId;
		targetInfo.level = (short) targetAttr.getLevel();
		targetInfo.shape = targetAttr.getShape();
		targetInfo.school = targetAttr.getSchool();
		mkdb.Procedure.psendWhileCommit(roleId, new SPvP1MatchResult(targetInfo));
	}
	
	/**
	 * 创建战斗
	 * @param grade
	 * @param hostRoleId
	 * @param guestRoleId
	 * @param now
	 */
	private static void NewBattle(final int grade, final long hostRoleId, final long guestRoleId, final long now) {
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
								PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(grade);
								if (race.getXRace() == null) {
									valid = false;
								}
								if (valid) {
									if (validRole(race, hostRoleId, now) == null) {
										valid = false;
									}
								}
								if (valid) {
									if (validRole(race, guestRoleId, now) == null) {
										valid = false;
									}
								}

								if (valid) {
									// 进入战斗
									mkdb.Procedure.pexecuteWhileCommit(new PPvP1NewBattle(hostRoleId, guestRoleId, grade));
								} else {
									// 客户端关闭倒计时界面
									mkdb.Procedure.psendWhileCommit(hostRoleId, new SPvP1ReadyFight((byte) 0));
									mkdb.Procedure.psendWhileCommit(guestRoleId, new SPvP1ReadyFight((byte) 0));
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
