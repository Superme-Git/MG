package fire.pb.battle.pvp1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import mkdb.logs.LogList;
import fire.pb.RoleConfigManager;
import fire.pb.talk.MessageMgr;

/**
 * 1v1赛场代理
 * @author XGM
 */
public class PvP1RaceProxy {
	/**
	 * 因为xbean.PvP1Race的数据量过大，不建议用select，所以直接不提供select的方法，暂时够用
	 * @param grade
	 * @return
	 */
	public static PvP1RaceProxy getPvP1RaceProxy(int grade) {

		boolean readonly = false;
		xbean.PvP1Race xrace = null;
		if (readonly) {
			xrace = xtable.Pvp1races.select(grade);
		} else {
			xrace = xtable.Pvp1races.get(grade);
		}
		return new PvP1RaceProxy(grade, xrace, readonly);
	}

	private final int grade;
	private xbean.PvP1Race xrace;
	private final boolean readonly;

	private PvP1RaceProxy(int grade, xbean.PvP1Race xrace, boolean readonly) {
		this.grade = grade;
		this.xrace = xrace;
		this.readonly = readonly;
	}

	public int getGrade() {
		return grade;
	}

	public xbean.PvP1Race getXRace() {
		return xrace;
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
		xrace = xbean.Pod.newPvP1Race();
		xtable.Pvp1races.remove(grade);
		xtable.Pvp1races.insert(grade, xrace);

		// 记录日志
		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[PvP1RaceProxy.begin] new race grade:" + grade);
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

		if (xrace == null)
			return false;

		// 踢出等待队列
		List<Long> roleinqueue = new LinkedList<Long>();
		for (xbean.PvP1QueueRole qrole : xrace.getWaitingqueue()) {
			roleinqueue.add(qrole.getRoleid());
		}
		mkdb.Procedure.psendWhileCommit(roleinqueue, new SPvP1ReadyFight((byte) 0));

		// 活动结束把非战斗,非观战玩家传出去
		for (Long roleId : xrace.getAllrolesid()) {
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_WATCHER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_REPLAY))
				continue;
			PvP1Control.getInstance().forceLeave(roleId);
		}

		// 清理数据
		//clear(); // 这里可以不清,等24点删除npc时再清 [4/12/2016 XGM]

		// 记录日志
		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[PvP1RaceProxy.end] grade:" + grade);
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

		xtable.Pvp1races.remove(grade);
		xrace = null;

		// 记录日志
		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[PvP1RaceProxy.clear] remove race grade:" + grade);
		}
	}

	/**
	 * 活动是否开启
	 * @return
	 */
	public boolean isOpening() {
		if (xrace == null) {
			return false;
		}
		if (PvP1Control.getInstance().isFightTime() == false) {
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

		if (isOpening() == false)
			return false;

		fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null) {
			return false;
		}

		int roleInRaceGrade = PvP1Helper.getRaceGradeByMapId(mapRole.getMapId());
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
	 * @return
	 */
	public boolean roleIsCanReady(long roleId) {

		// 检测活动时间
		if (isOpening() == false) {
			return false;
		}
		// 检测角色
		if (roleIsValid(roleId) == false) {
			return false;
		}
		// 检测队伍
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_TEAM)) {
			MessageMgr.psendMsgNotify(roleId, 144300, null);
			return false;
		}
		// 检测伙伴
		int error = checkHuoBan(roleId);
		if (error != 0) {
			switch (error) {
			case -1:
			case -2:
			default:
				MessageMgr.psendMsgNotify(roleId, 160496, null);
				break;
			case -3:
				MessageMgr.psendMsgNotify(roleId, 160333, null);
				break;
			}
			return false;
		}
		// 检测战斗次数
		PvP1RoleProxy pvpRole = PvP1RoleProxy.getPvP1RoleProxy(roleId, true);
		if (pvpRole.getBattleNum() >= PvP1Helper.RACE_MAX_BATTLE_NUM) {
			MessageMgr.psendMsgNotify(roleId, 144297, MessageMgr.getStringList(PvP1Helper.RACE_MAX_BATTLE_NUM));
			return false;
		}
		// 检测是否在战斗
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			return false;
		}

		return true;
	}

	/**
	 * 检测伙伴
	 * @param roleId
	 * @return
	 */
	public static int checkHuoBan(long roleId) {

		fire.pb.huoban.HuoBanColumn huoBanCol = fire.pb.huoban.HuoBanColumn.getHuoBanColumn(roleId, true);
		if (huoBanCol == null)
			return -1;

		// 控制、辅助、治疗类职业出战单位总和不可超过3个
		List<Integer> keys = huoBanCol.getFightHuobanKeys();
		if (keys.size() < 4) {
			return -2;
		}
		int count = 0;
		for (Integer key : keys) {
			fire.pb.huoban.HuoBan huoban = huoBanCol.getHuoBan(key);
			if (huoban != null && huoban.getType() != 1 && huoban.getType() != 2) {
				++count;
			}
		}
		// 加上自己的职业
		int school = xtable.Properties.selectSchool(roleId);
		fire.pb.role.RoleConfig roleConfig = RoleConfigManager.getRoleConfigBySchoolID(school);
		if (roleConfig != null && roleConfig.getType() != 1 && roleConfig.getType() != 2) {
			++count;
		}
		if (count > 3) {
			return -3;
		}
		return 0;
	}

	/**
	 * 角色进入赛场
	 * @param roleId
	 * @return
	 */
	public boolean roleEnter(long roleId) {

		if (xrace == null) {
			return false;
		}

		PvP1RoleProxy pvpRole = PvP1RoleProxy.getPvP1RoleProxy(roleId, false);
		// 进入赛场后的处理
		xrace.getAllrolesid().remove(roleId);
		xrace.getAllrolesid().add(roleId);
		pvpRole.onEnter(grade);

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
		if (xrace != null) {
			xrace.getAllrolesid().remove(roleId);
		}
		return true;
	}

	/**
	 * 角色准备好了
	 * @param roleId
	 * @return
	 */
	public boolean roleReady(long roleId) {

		if (isOpening() == false) {
			MessageMgr.sendMsgNotify(roleId, 144298, null);
			return false;
		}

		long now = System.currentTimeMillis();

		if (roleIsValid(roleId) == false) {
			return false;
		}

		if (roleIsCanReady(roleId) == false) {
			return false;
		}

		// 加入等待序列
		boolean isWaiting = false;
		for (xbean.PvP1QueueRole role : xrace.getWaitingqueue()) {
			if (role.getRoleid() == roleId) {
				isWaiting = true;
			}
		}
		if (isWaiting == false) {
			xbean.PvP1QueueRole qrole = xbean.Pod.newPvP1QueueRole();
			qrole.setRoleid(roleId);
			qrole.setEnterqueuetime(now);
			xrace.getWaitingqueue().add(qrole);
		}

		// 通知客户端
		mkdb.Procedure.psendWhileCommit(roleId, new SPvP1ReadyFight((byte) 1));

		// 接下来等待定时匹配结果
		return true;
	}
	
	/**
	 * 取消准备
	 * @param roleId
	 * @return
	 */
	public boolean roleUnready(long roleId) {

		if (xrace == null)
			return false;

		// 删除等待,取消准备
		if (removeRoleFromWaitingQueue(roleId) != null) {
			mkdb.Procedure.psendWhileCommit(roleId, new SPvP1ReadyFight((byte) 0));
		}
		return true;
	}

	/**
	 * 更新排行榜
	 * @param roleId 角色id
	 * @param name 角色名称
	 * @param score 角色积分
	 * @param battleNum 战斗的次数
	 * @param winNum 赢的次数
	 * @param continueWinNum 连胜次数
	 */
	public void updateRankingList(long roleId, String name, int addScore,
			int score, int battleNum, int winNum, int continueWinNum) {

		if (xrace == null) {
			return;
		}

		// 更新积分排行
		updateScoreRankingList(roleId, name, addScore, score, battleNum, winNum);
	}

	/**
	 * 更新积分榜
	 */
	private void updateScoreRankingList(long roleId, String name, int addScore,
			int score, int battleNum, int winNum) {

		// 积分不变的情况下,不更新排行榜名次
		if (addScore == 0) {
			xbean.PvP1RaceRole raceRole = getRoleFromScoreRankingList(roleId);
			if (raceRole != null) {
				raceRole.setBattlenum(battleNum);
				raceRole.setWinnum(winNum);
				return;
			}
		}

		// 先删除
		xbean.PvP1RaceRole delRole = removeRoleFromScoreRankingList(roleId);

		// 再添加
		ListIterator<xbean.PvP1RaceRole> listIt = ((LogList<xbean.PvP1RaceRole>) (xrace.getAllroles())).listIterator();
		for (; listIt.hasNext();) {
			xbean.PvP1RaceRole racerole = listIt.next();
			if (score > racerole.getScore()) {
				listIt.previous();
				if (delRole == null) {
					delRole = xbean.Pod.newPvP1RaceRole();
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

		// 加到末尾或者队列中的第一个人
		if (delRole == null) {
			delRole = xbean.Pod.newPvP1RaceRole();
			delRole.setRoleid(roleId);
			delRole.setName(name);
		}
		delRole.setScore(score);
		delRole.setBattlenum(battleNum);
		delRole.setWinnum(winNum);
		xrace.getAllroles().add(delRole);
	}

	/**
	 * 从积分榜获取一个角色
	 */
	private xbean.PvP1RaceRole getRoleFromScoreRankingList(final long roleId) {

		if (xrace == null)
			return null;

		for (xbean.PvP1RaceRole raceRole : xrace.getAllroles()) {
			if (roleId == raceRole.getRoleid()) {
				return raceRole;
			}
		}
		return null;
	}

	/**
	 * 从积分榜删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP1RaceRole removeRoleFromScoreRankingList(final long roleId) {

		if (xrace == null)
			return null;

		for (Iterator<xbean.PvP1RaceRole> it = xrace.getAllroles().iterator(); it.hasNext();) {
			xbean.PvP1RaceRole racerole = it.next();
			if (roleId == racerole.getRoleid()) {
				it.remove();
				return racerole;
			}
		}
		return null;
	}

	/**
	 * 从等待队列删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP1QueueRole removeRoleFromWaitingQueue(final long roleId) {

		if (xrace == null)
			return null;

		for (Iterator<xbean.PvP1QueueRole> it = xrace.getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP1QueueRole racerole = it.next();
			if (roleId == racerole.getRoleid()) {
				it.remove();
				return racerole;
			}
		}
		return null;
	}

	/**
	 * 发送排行榜
	 * @param roleId
	 */
	public void sendSPvP1RankingList(long roleId) {

		if (isOpening() == false)
			return;

		SPvP1RankingList send = new SPvP1RankingList();

		int max = 50;
		int i = 0;
		boolean findMyself = false;

		PvP1RoleSingleScoreMid self = new PvP1RoleSingleScoreMid();

		for (xbean.PvP1RaceRole raceRole : xrace.getAllroles()) {
			if (i < max) {
				fire.pb.battle.pvp1.PvP1RoleSingleScore singleScore = new fire.pb.battle.pvp1.PvP1RoleSingleScore();
				singleScore.roleid = raceRole.getRoleid();
				singleScore.rolename = raceRole.getName();
				singleScore.score = raceRole.getScore();
				singleScore.battlenum = (byte) raceRole.getBattlenum();
				singleScore.winnum = (byte) raceRole.getWinnum();
				send.rolescores.add(singleScore);
			}
			if (raceRole.getRoleid() == roleId) {
				self.index = (short) (i + 1);
				self.roleid = roleId;
				self.rolename = raceRole.getName();
				self.score = raceRole.getScore();
				self.battlenum = (byte) raceRole.getBattlenum();
				self.winnum = (byte) raceRole.getWinnum();
				findMyself = true;
			}
			i++;
			if (i >= max && findMyself) {
				break;
			}
		}

		if (findMyself) {
			if (self.index > max) {
				self.index = 0; // 超出50显示榜外
			}
			send.rolescores3.add(self);
		}

		mkdb.Procedure.psendWhileCommit(roleId, send);
	}

	/**
	 * 获取需要广播的在赛场中的所有玩家(不包括战斗中的)
	 * @param except
	 * @return
	 */
	public Set<Long> getBroadcastRoles(Set<Long> except) {

		Set<Long> allRoleIds = new HashSet<Long>();
		for (long roleId : xrace.getAllrolesid()) {
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
	 * 是否准备
	 * @param roleId
	 * @return
	 */
	public boolean isReady(long roleId) {

		for (xbean.PvP1QueueRole role : xrace.getWaitingqueue()) {
			if (role.getRoleid() == roleId)
				return true;
		}
		return false;
	}
}
