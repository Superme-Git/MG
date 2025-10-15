package fire.pb.battle.pvp5;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import mkdb.logs.LogList;
import fire.pb.battle.pvp.PvPRaceProxy;
import fire.pb.battle.pvp.PvPResult;
import fire.pb.talk.MessageMgr;

/**
 * 5v5赛场代理
 * @author XGM
 */
public class PvP5RaceProxy extends PvPRaceProxy {

	/**
	 * 获取PvP赛场代理
	 * 因为xbean.PvP5Race的数据量过大,不建议用select,所以直接不提供select的方法,暂时够用
	 * @param grade
	 * @return
	 */
	public static PvP5RaceProxy getPvP5RaceProxy(int grade) {
		boolean readonly = false;
		xbean.PvP5Race xpvprace = null;
		if (readonly) {
			xpvprace = xtable.Pvp5races.select(grade);
		} else {
			xpvprace = xtable.Pvp5races.get(grade);
		}
		return new PvP5RaceProxy(grade, xpvprace, readonly);
	}

	/**
	 * 获取某一个阵营的人数
	 * @param grade
	 * @param camp
	 * @return
	 */
	public static int getRoleNumByCamp(int grade, int camp) {
		int roleNum = 0;
		List<?> list = null;
		switch (camp) {
		case 0:
			list = xtable.Pvp5races.selectAllrolesidcampa(grade);
			break;
		case 1:
			list = xtable.Pvp5races.selectAllrolesidcampb(grade);
			break;
		}
		if (list != null) {
			roleNum = list.size();
		}
		return roleNum;
	}

	/**
	 * 比赛是否结束
	 * @param grade
	 * @return
	 */
	public static boolean isEnd(int grade) {
		Integer v = xtable.Pvp5races.selectIsend(grade);
		return v == null || v == 1;
	}

	private final int grade;
	private xbean.PvP5Race xpvprace;
	private final boolean readonly;

	/**
	 * 构造
	 * @param grade
	 * @param xpvprace
	 * @param readonly
	 */
	public PvP5RaceProxy(int grade, xbean.PvP5Race xpvprace, boolean readonly) {
		this.grade = grade;
		this.xpvprace = xpvprace;
		this.readonly = readonly;
	}

	/**
	 * 获取比赛等级
	 * @return
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * 获取赛场
	 * @return
	 */
	public xbean.PvP5Race getXRace() {
		return xpvprace;
	}

	/**
	 * 开始一场比赛
	 * @return
	 */
	public boolean begin() {

		if (readonly) {
			throw new UnsupportedOperationException("cannot invoke while readonly");
		}

		// 结束当前的
		end();

		// 创建新的
		xpvprace = xbean.Pod.newPvP5Race();
		xtable.Pvp5races.remove(grade);
		xtable.Pvp5races.insert(grade, xpvprace);

		// 记录日志
		if (PvP5Control.getLogger().isInfoEnabled()) {
			PvP5Control.getLogger().info("PVP5::[PvP5RaceProxy.begin] new race grade:" + grade);
		}
		return true;
	}

	/**
	 * 结束当前比赛
	 * @return
	 */
	public boolean end() {

		if (readonly) {
			throw new UnsupportedOperationException("cannot invoke while readonly");
		}

		if (xpvprace == null)
			return false;

		for (xbean.PvP5QueueRole role : xpvprace.getWaitingqueue()) {
			// 关闭匹配状态
			PvP5RoleProxy.setTeamMemberPvPState(role.getRoleid(), PvP5RoleStateManager.PVPSTATE_DEFAULT);
		}
		xpvprace.getWaitingqueue().clear();

		// 活动结束把非战斗,非观战玩家传出去
		List<Long> roleIds = new LinkedList<Long>();
		roleIds.addAll(xpvprace.getAllrolesidcampa());
		roleIds.addAll(xpvprace.getAllrolesidcampb());
		for (Long roleId : roleIds) {
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_WATCHER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_REPLAY))
				continue;
			PvP5Control.getInstance().forceLeave(roleId);
		}

		// 已经决出胜负
		xpvprace.setIsend(1);

		// 清理数据
		//clear(); // 这里可以不清,等24点删除npc时,同步一下排行榜数据,再清 [3/29/2016 XGM]

		// 记录日志
		if (PvP5Control.getLogger().isInfoEnabled()) {
			PvP5Control.getLogger().info("PVP5::[PvP5RaceProxy.end] grade:" + grade);
		}
		return true;
	}

	/**
	 * 清理
	 */
	public void clear() {

		if (readonly) {
			return;
		}

		xtable.Pvp5races.remove(grade);
		xpvprace = null;

		// 记录日志
		if (PvP5Control.getLogger().isInfoEnabled()) {
			PvP5Control.getLogger().info("PVP5::[PvP5RaceProxy.clear] remove race grade:" + grade);
		}
	}

	/**
	 * 活动是否开启
	 * @return
	 */
	public boolean isOpening() {
		if (xpvprace == null) {
			return false;
		}
		if (xpvprace.getIsend() == 1) {
			return false;
		}
		if (PvP5Control.getInstance().isFightTime() == false) {
			return false;
		}
		return true;
	}

	/**
	 * 角色是不是有效的
	 * @param roleId
	 * @return
	 */
	public boolean roleIsValid(long roleId) {

		if (isOpening() == false) {
			return false;
		}

		fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null) {
			return false;
		}

		int roleInRaceGrade = PvP5Helper.getRaceGradeByMapId(mapRole.getMapId());
		if (roleInRaceGrade <= 0) {
			return false;
		}
		if (roleInRaceGrade != this.grade) {
			return false;
		}
		return true;
	}

	/**
	 * 角色是否能准备
	 * @param roleId
	 * @param isCheckTeam
	 * @return
	 */
	public boolean roleIsCanReady(long roleId, boolean isCheckTeam) {

		// 检测活动时间
		if (isOpening() == false) {
			return false;
		}
		// 检测角色
		if (roleIsValid(roleId) == false) {
			return false;
		}
		//
		PvP5RoleProxy pvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
		// 检测队伍
		if (isCheckTeam) {
			int mapId = PvP5Helper.getMapIdByRaceGrade(getGrade(), (pvpRoleProxy.getExcellent() == 1), pvpRoleProxy.getCamp());
			PvPResult result = PvP5Helper.checkRoleEnterCondition(roleId, getGrade(), pvpRoleProxy.getCamp(), mapId);
			if (result.get() != 0) {
				result.handle();
				return false;
			}
		}
		// 检测战斗次数
		if (pvpRoleProxy.getBattleNum() >= PvP5Helper.RACE_MAX_BATTLE_NUM) {
			MessageMgr.psendMsgNotify(roleId, 144297, MessageMgr.getStringList(PvP5Helper.RACE_MAX_BATTLE_NUM));
			return false;
		}
		// 检测是否在战斗
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			return false;
		}

		return true;
	}

	/**
	 * 角色进入赛场
	 * @param roleId
	 * @param mapId
	 * @return
	 */
	public boolean roleEnter(long roleId, int mapId) {

		if (xpvprace == null) {
			return false;
		}
		if (xpvprace.getIsend() == 1) {
			PvP5Control.getInstance().forceLeave(roleId);
			return true;
		}

		// 这里要判断一下角色是否能够进入比赛场地,参考1v1
		//

		int camp = PvP5Helper.getCampByMapId(mapId);

		PvP5RoleProxy pvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, false);
		// 进入赛场后的处理
		// 分阵营
		if (camp == 0) {
			xpvprace.getAllrolesidcampa().remove(roleId);
			xpvprace.getAllrolesidcampa().add(roleId);
		} else {
			xpvprace.getAllrolesidcampb().remove(roleId);
			xpvprace.getAllrolesidcampb().add(roleId);
		}

		pvpRoleProxy.onEnter(grade, mapId);

		return true;
	}

	/**
	 * 角色离开赛场
	 * @param roleId
	 * @return
	 */
	public boolean roleLeave(long roleId) {

		// 从等待队列删除
		removeRoleFromWaitingQueue(roleId);

		// 删除角色ID
		if (xpvprace != null) {
			xpvprace.getAllrolesidcampa().remove(roleId);
			xpvprace.getAllrolesidcampb().remove(roleId);
		}
		return true;
	}

	/**
	 * 更新排行榜
	 * @param roleId
	 * @param name
	 * @param score
	 * @param battleNum
	 * @param winNum
	 */
	public void updateRankingList(long roleId, String name, int addScore, int score, int battleNum, int winNum) {

		if (xpvprace == null) {
			return;
		}

		updateScoreRankingList(roleId, name, addScore, score, battleNum, winNum);
	}

	/**
	 * 更新积分榜
	 * @param roleId
	 * @param name
	 * @param score
	 * @param battleNum
	 * @param winNum
	 */
	public void updateScoreRankingList(long roleId, String name, int addScore, int score, int battleNum, int winNum) {

		// 积分不变的情况下,不更新排行榜名次
		if (addScore == 0) {
			xbean.PvP5RaceRole raceRole = getRoleFromScoreRankingList(roleId);
			if (raceRole != null) {
				raceRole.setBattlenum(battleNum);
				raceRole.setWinnum(winNum);
				return;
			}
		}

		// 先删除
		xbean.PvP5RaceRole delRole = removeRoleFromScoreRankingList(roleId);

		ListIterator<xbean.PvP5RaceRole> listIt = null;
		if (xpvprace.getAllrolesidcampa().contains(roleId)) {
			listIt = ((LogList<xbean.PvP5RaceRole>) xpvprace.getAllrolescampa()).listIterator();
		} else if (xpvprace.getAllrolesidcampb().contains(roleId)) {
			listIt = ((LogList<xbean.PvP5RaceRole>) xpvprace.getAllrolescampb()).listIterator();
		}
		if (listIt != null) {
			// 再添加
			while (listIt.hasNext()) {
				xbean.PvP5RaceRole role = listIt.next();
				if (score > role.getScore()) {
					listIt.previous();
					if (delRole == null) {
						delRole = xbean.Pod.newPvP5RaceRole();
						delRole.setRoleid(roleId);
						delRole.setName(name);
					}
					delRole.setScore(score);
					delRole.setBattlenum(battleNum);
					delRole.setWinnum(winNum);
					listIt.add(delRole);
					return;
				}
			}
		}

		// 加到末尾或者队列中的第一个人
		if (delRole == null) {
			delRole = xbean.Pod.newPvP5RaceRole();
			delRole.setRoleid(roleId);
			delRole.setName(name);
		}
		delRole.setScore(score);
		delRole.setBattlenum(battleNum);
		delRole.setWinnum(winNum);

		if (xpvprace.getAllrolesidcampa().contains(roleId)) {
			xpvprace.getAllrolescampa().add(delRole);
		} else if (xpvprace.getAllrolesidcampb().contains(roleId)) {
			xpvprace.getAllrolescampb().add(delRole);
		}
	}

	/**
	 * 从积分榜获取一个角色
	 */
	private xbean.PvP5RaceRole getRoleFromScoreRankingList(long roleId) {

		if (xpvprace == null)
			return null;

		List<xbean.PvP5RaceRole> list = null;
		if (xpvprace.getAllrolesidcampa().contains(roleId)) {
			list = xpvprace.getAllrolescampa();
		} else if (xpvprace.getAllrolesidcampb().contains(roleId)) {
			list = xpvprace.getAllrolescampb();
		}
		if (list != null) {
			for (xbean.PvP5RaceRole raceRole : list) {
				if (roleId == raceRole.getRoleid()) {
					return raceRole;
				}
			}
		}
		return null;
	}

	/**
	 * 从积分榜删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP5RaceRole removeRoleFromScoreRankingList(long roleId) {

		if (xpvprace == null)
			return null;

		Iterator<xbean.PvP5RaceRole> it = null;
		if (xpvprace.getAllrolesidcampa().contains(roleId)) {
			it = xpvprace.getAllrolescampa().iterator();
		} else if (xpvprace.getAllrolesidcampb().contains(roleId)) {
			it = xpvprace.getAllrolescampb().iterator();
		}
		if (it != null) {
			while (it.hasNext()) {
				xbean.PvP5RaceRole role = it.next();
				if (roleId == role.getRoleid()) {
					it.remove();
					return role;
				}
			}
		}
		return null;
	}

	/**
	 * 从等待队列删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP5QueueRole removeRoleFromWaitingQueue(long roleId) {

		if (xpvprace == null)
			return null;

		for (Iterator<xbean.PvP5QueueRole> it = xpvprace.getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP5QueueRole role = it.next();
			if (roleId == role.getRoleid()) {
				it.remove();
				return role;
			}
		}
		return null;
	}

	/**
	 * 发送排行榜
	 * @param roleId
	 */
	public void sendSPvP5RankingList(long roleId) {

		if (isOpening() == false)
			return;

		SPvP5RankingList send = new SPvP5RankingList();
		PvP5RoleSingleScoreMid self = new PvP5RoleSingleScoreMid();

		if (xpvprace.getAllrolesidcampa().contains(roleId)) {
			self.listid = 1;
			fullMyRoleScore(roleId, self, xpvprace.getAllrolescampa());
		} else if (xpvprace.getAllrolesidcampb().contains(roleId)) {
			self.listid = 2;
			fullMyRoleScore(roleId, self, xpvprace.getAllrolescampb());
		}

		int max = 50;
		fullRoleScoreList(send.rolescores1, xpvprace.getAllrolescampa(), 1, max);
		fullRoleScoreList(send.rolescores2, xpvprace.getAllrolescampb(), 2, max);

		if (self.index != 0) {
			if (self.index > max) {
				self.index = 0; // 超出50显示榜外
			}
			send.myscore = self;
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}

	/**
	 * 填充排行榜列表
	 */
	private static void fullRoleScoreList(List<PvP5RoleSingleScore> roleScoreList,
			List<xbean.PvP5RaceRole> roleCampList, int listId, int max) {

		int i = 0;

		for (xbean.PvP5RaceRole raceRole : roleCampList) {
			if (raceRole == null)
				continue;
			if (i < max) {
				PvP5RoleSingleScore prole = new PvP5RoleSingleScore();
				prole.roleid = raceRole.getRoleid();
				prole.rolename = raceRole.getName();
				prole.score = raceRole.getScore();
				prole.battlenum = (byte) raceRole.getBattlenum();
				prole.winnum = (byte) raceRole.getWinnum();
				roleScoreList.add(prole);
			}

			i++;
			if (i >= max) {
				break;
			}
		}
	}

	/**
	 * 填充自己的排名信息
	 */
	private static void fullMyRoleScore(Long roleId,
			PvP5RoleSingleScoreMid roleScore,
			List<xbean.PvP5RaceRole> roleCampList) {

		int i = 0;
		for (xbean.PvP5RaceRole raceRole : roleCampList) {
			if (raceRole == null)
				continue;
			if (raceRole.getRoleid() == roleId) {
				roleScore.index = (short) (i + 1);
				roleScore.roleid = roleId;
				roleScore.rolename = raceRole.getName();
				roleScore.score = raceRole.getScore();
				roleScore.battlenum = (byte) raceRole.getBattlenum();
				roleScore.winnum = (byte) raceRole.getWinnum();
				return;
			}
			i++;
		}
	}

	/**
	 * 获取需要广播的在赛场中的所有玩家(不包括战斗中的)
	 * @param except
	 * @return
	 */
	public Set<Long> getBroadcastRoles(Set<Long> except) {

		Set<Long> allRoleIds = new HashSet<Long>();
		List<Long> roleIds = new LinkedList<Long>();
		roleIds.addAll(xpvprace.getAllrolesidcampa());
		roleIds.addAll(xpvprace.getAllrolesidcampb());
		for (long roleId : roleIds) {
			if (except.contains(roleId))
				continue;
			if (roleIsValid(roleId) == false)
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				continue;
			allRoleIds.add(roleId);
		}
		return allRoleIds;
	}

	/**
	 * 获取进入等待队列时间
	 * @param roleId
	 * @return -1:不在等待中, >0:等待的起始时间
	 */
	public long getEnterWaitingQueueTime(long roleId) {

		if (xpvprace == null) {
			return -1;
		}

		long leaderId = roleId;
		List<Long> ids = PvP5RoleProxy.getTeamMemberIds(roleId);
		if (ids.size() > 1) {
			leaderId = ids.get(0);
		}

		for (xbean.PvP5QueueRole role : xpvprace.getWaitingqueue()) {
			if (role.getRoleid() == leaderId) {
				return role.getEnterqueuetime();
			}
		}
		return -1;
	}

}
