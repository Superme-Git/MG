package fire.pb.battle.pvp3;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import mkdb.logs.LogList;
import fire.pb.battle.pvp.PvPRaceProxy;
import fire.pb.talk.MessageMgr;

/**
 * 3v3赛场代理
 * @author XGM
 */
public class PvP3RaceProxy extends PvPRaceProxy {

	/**
	 * 获取PvP赛场代理
	 * 因为xbean.PvP3Race的数据量过大，不建议用select，所以直接不提供select的方法，暂时够用
	 * @param grade
	 * @return
	 */
	public static PvP3RaceProxy getPvP3RaceProxy(int grade) {
		boolean readonly = false;
		xbean.PvP3Race xpvprace = null;
		if (readonly) {
			xpvprace = xtable.Pvp3races.select(grade);
		} else {
			xpvprace = xtable.Pvp3races.get(grade);
		}
		return new PvP3RaceProxy(grade, xpvprace, readonly);
	}

	private final int grade;
	private xbean.PvP3Race xpvprace;
	private final boolean readonly;

	/**
	 * 构造
	 * @param grade
	 * @param xpvprace
	 * @param readonly
	 */
	public PvP3RaceProxy(int grade, xbean.PvP3Race xpvprace, boolean readonly) {
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
	public xbean.PvP3Race getXRace() {
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
		xpvprace = xbean.Pod.newPvP3Race();
		xtable.Pvp3races.remove(grade);
		xtable.Pvp3races.insert(grade, xpvprace);

		// 记录日志
		if (PvP3Control.getLogger().isInfoEnabled()) {
			PvP3Control.getLogger().info("PVP3::[PvP3RaceProxy.begin] new race grade:" + grade);
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

		// 踢出等待队列
		List<Long> roleQueue = new LinkedList<Long>();
		for(xbean.PvP3QueueRole role : xpvprace.getWaitingqueue()) {
			roleQueue.add(role.getRoleid());
			// 关闭匹配状态
			PvP3RoleProxy.setTeamMemberPvPState(role.getRoleid(), PvP3RoleStateManager.PVPSTATE_DEFAULT);
		}
		// 通知客户端
		mkdb.Procedure.psendWhileCommit(roleQueue, new SPvP3ReadyFight((byte)0));

		// 活动结束把非战斗,非观战玩家传出去
		for (Long roleId : xpvprace.getAllrolesid()) {
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_WATCHER))
				continue;
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_REPLAY))
				continue;
			PvP3Control.getInstance().forceLeave(roleId);
		}

		// 清理数据
		//clear(); // 这里可以不清,等24点删除npc时再清 [4/12/2016 XGM]

		// 记录日志
		if (PvP3Control.getLogger().isInfoEnabled()) {
			PvP3Control.getLogger().info("PVP3::[PvP3RaceProxy.end] grade:" + grade);
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

		xtable.Pvp3races.remove(grade);
		xpvprace = null;

		// 记录日志
		if (PvP3Control.getLogger().isInfoEnabled()) {
			PvP3Control.getLogger().info("PVP3::[PvP3RaceProxy.clear] remove race grade:" + grade);
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
		if (PvP3Control.getInstance().isFightTime() == false) {
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
		if(mapRole == null) {
			return false;
		}

		int roleInRaceGrade = PvP3Helper.getRaceGradeByMapId(mapRole.getMapId());
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
		// 检测队伍
		if (isCheckTeam) {
			int error = PvP3Helper.checkRoleEnterCondition(roleId, getGrade());
			if (error != 0) {
				switch (error) {
				case -1: // 只有队长才能操作
					MessageMgr.sendMsgNotify(roleId, 160338, null);
					break;
				case -2: // 队伍人数超出3人
					MessageMgr.sendMsgNotify(roleId, 160602, null);
					break;
				case -4: // 队员等级不匹配
				default:
					MessageMgr.sendMsgNotify(roleId, 160339, null);
					break;
				}
				return false;
			}
		}
		// 跨服PVP3必须3人才能匹配
		if (fire.pb.PropConf.Cross.PVP == 1) {
			if (PvP3RoleProxy.getTeamMemberIds(roleId).size() != PvP3Helper.TEAM_MAX_MEMBER_NUM) {
				MessageMgr.sendMsgNotify(roleId, 172040, null);
				return false;
			}
		}
		// 检测战斗次数
		PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(roleId, true);
		if (pvpRoleProxy.getBattleNum() >= PvP3Helper.RACE_MAX_BATTLE_NUM) {
			MessageMgr.psendMsgNotify(roleId, 144297, MessageMgr.getStringList(PvP3Helper.RACE_MAX_BATTLE_NUM));
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
	 * @return
	 */
	public boolean roleEnter(long roleId) {

		if (xpvprace == null) {
			return false;
		}

		// 这里要判断一下角色是否能够进入比赛场地,参考1v1
		//

		PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(roleId, false);
		//进入赛场后的处理
		xpvprace.getAllrolesid().remove(roleId);
		xpvprace.getAllrolesid().add(roleId);
		pvpRoleProxy.onEnter(grade);

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
			xpvprace.getAllrolesid().remove(roleId);
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

		if (roleIsValid(roleId) == false) {
			return false;
		}

		if (roleIsCanReady(roleId, true) == false) {
			return false;
		}

		long now = System.currentTimeMillis();

		// 加入等待序列
		boolean isWaiting = false;
		for(xbean.PvP3QueueRole role : xpvprace.getWaitingqueue()){
			if(role.getRoleid() == roleId){
				isWaiting = true;
			}
		}
		if (isWaiting == false) {
			xbean.PvP3QueueRole role = xbean.Pod.newPvP3QueueRole();
			role.setRoleid(roleId);
			role.setEnterqueuetime(now);
			xpvprace.getWaitingqueue().add(role);

			// 设置匹配状态
			PvP3RoleProxy.setTeamMemberPvPState(roleId, PvP3RoleStateManager.PVPSTATE_MATCH);
		}

		// 通知客户端
		PvP3RoleProxy.notifyTeamProtocol(roleId, new SPvP3ReadyFight((byte)1));

		// 接下来等待定时匹配结果
		return true;
	}

	/**
	 * 角色取消准备
	 * @param roleId
	 * @return
	 */
	public boolean roleUnready(long roleId) {

		if (xpvprace == null)
			return false;

		// 删除等待,取消准备
		if (removeRoleFromWaitingQueue(roleId) != null) {
			// 关闭匹配状态
			PvP3RoleProxy.setTeamMemberPvPState(roleId, PvP3RoleStateManager.PVPSTATE_DEFAULT);
			// 通知客户端
			PvP3RoleProxy.notifyTeamProtocol(roleId, new SPvP3ReadyFight((byte) 0));
		}
		return true;
	}

	/**
	 * 更新排行榜
	 * @param roleId
	 * @param name
	 * @param score
	 */
	public void updateRankingList(long roleId, String name, int addScore, int score) {

		if (xpvprace == null) {
			return;
		}

		updateScoreRankingList(roleId, name, addScore, score);
	}

	/**
	 * 更新积分榜
	 * @param roleId
	 * @param name
	 * @param score
	 */
	public void updateScoreRankingList(long roleId, String name, int addScore, int score) {

		// 积分不变的情况下,不更新排行榜名次
		if (addScore == 0) {
			return;
		}

		// 先删除
		xbean.PvP3RaceRole delRole = removeRoleFromScoreRankingList(roleId);

		// 再添加
		ListIterator<xbean.PvP3RaceRole> listIt = ((LogList<xbean.PvP3RaceRole>) xpvprace.getAllroles()).listIterator();
		for (; listIt.hasNext();) {
			xbean.PvP3RaceRole role = listIt.next();
			if (score > role.getScore()) {
				listIt.previous();
				if (delRole == null) {
					delRole = xbean.Pod.newPvP3RaceRole();
					delRole.setRoleid(roleId);
					delRole.setName(name);
				}
				delRole.setScore(score);
				listIt.add(delRole);
				return;
			}
		}

		// 加到末尾或者队列中的第一个人
		if (delRole == null) {
			delRole = xbean.Pod.newPvP3RaceRole();
			delRole.setRoleid(roleId);
			delRole.setName(name);
		}
		delRole.setScore(score);
		xpvprace.getAllroles().add(delRole);
	}

	/**
	 * 从积分榜删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP3RaceRole removeRoleFromScoreRankingList(long roleId) {

		if (xpvprace == null)
			return null;

		for (Iterator<xbean.PvP3RaceRole> it = xpvprace.getAllroles().iterator(); it.hasNext();) {
			xbean.PvP3RaceRole role = it.next();
			if (roleId == role.getRoleid()) {
				it.remove();
				return role;
			}
		}
		return null;
	}

	/**
	 * 从等待队列删除一个角色
	 * @param roleId
	 * @return
	 */
	private xbean.PvP3QueueRole removeRoleFromWaitingQueue(long roleId) {

		if (xpvprace == null)
			return null;

		for (Iterator<xbean.PvP3QueueRole> it = xpvprace.getWaitingqueue().iterator(); it.hasNext();) {
			xbean.PvP3QueueRole role = it.next();
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
	public void sendSPvP3RankingList(long roleId) {

		if (isOpening() == false)
			return;

		SPvP3RankingList send = new SPvP3RankingList();
		send.history = 0;

		int max = 50;
		int i = 0;
		boolean findMyself = false;

		PvP3RoleSingleScoreMid self = new PvP3RoleSingleScoreMid();

		for (xbean.PvP3RaceRole raceRole : xpvprace.getAllroles()) {
			if (i < max) {
				fire.pb.battle.pvp3.PvP3RoleSingleScore singleScore = new fire.pb.battle.pvp3.PvP3RoleSingleScore();
				singleScore.roleid = raceRole.getRoleid();
				singleScore.rolename = raceRole.getName();
				singleScore.score = raceRole.getScore();
				send.rolescores.add(singleScore);
			}
			if (raceRole.getRoleid() == roleId) {
				self.index = (short) (i + 1);
				self.roleid = roleId;
				self.rolename = raceRole.getName();
				self.score = raceRole.getScore();
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
			send.myscore.add(self);
		}

		mkdb.Procedure.psendWhileCommit(roleId, send);
	}

	/**
	 * 发送历史排行榜
	 * @param roleId
	 */
	public void sendSPvP3RankingListOfHistory(long roleId) {

		if (isOpening() == false)
			return;

		SPvP3RankingList send = new SPvP3RankingList();
		send.history = 1;

		int max = 50;
		int i = 0;
		boolean findMyself = false;

		PvP3RoleSingleScoreMid self = new PvP3RoleSingleScoreMid();

		xbean.PvP3ScoreRankList rankList = xtable.Pvp3historyscorelist.select(getGrade());
		if (rankList == null) {
			return;
		}
		for (xbean.PvP3ScoreRecord record : rankList.getRecords()) {
			if (i < max) {
				fire.pb.battle.pvp3.PvP3RoleSingleScore singleScore = new fire.pb.battle.pvp3.PvP3RoleSingleScore();
				singleScore.roleid = record.getRoleid();
				singleScore.rolename = record.getRolename();
				singleScore.score = record.getScore();
				send.rolescores.add(singleScore);
			}
			if (record.getRoleid() == roleId) {
				self.index = (short) (i + 1);
				self.roleid = roleId;
				self.rolename = record.getRolename();
				self.score = record.getScore();
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
			send.myscore.add(self);
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
		for (long roleId : xpvprace.getAllrolesid()) {
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

		if (xpvprace == null) {
			return false;
		}

		long leaderId = roleId;
		List<Long> ids = PvP3RoleProxy.getTeamMemberIds(roleId);
		if (ids.size() > 1) {
			leaderId = ids.get(0);
		}

		for (xbean.PvP3QueueRole role : xpvprace.getWaitingqueue()) {
			if (role.getRoleid() == leaderId) {
				return true;
			}
		}
		return false;
	}
}
