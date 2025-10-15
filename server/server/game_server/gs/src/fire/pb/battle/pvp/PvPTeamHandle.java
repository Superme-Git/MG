package fire.pb.battle.pvp;

import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp3.PvP3Control;
import fire.pb.battle.pvp3.PvP3Helper;
import fire.pb.battle.pvp3.PvP3RoleStateManager;
import fire.pb.battle.pvp5.PvP5Control;
import fire.pb.battle.pvp5.PvP5Helper;
import fire.pb.battle.pvp5.PvP5RoleStateManager;
import fire.pb.talk.MessageMgr;

/**
 * PvP队伍处理
 * @author XGM
 */
public class PvPTeamHandle {

	/**
	 * 暂离或回归
	 * @param roleId 角色id
	 * @param absent 1表示暂离 0表示回归
	 * @return
	 */
	public static int onAbsentReturn(long roleId, byte absent) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(roleId) == false) {
				MessageMgr.sendMsgNotify(roleId, 141618, null);
				return -20;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(roleId) == false) {
				MessageMgr.sendMsgNotify(roleId, 141618, null);
				return -30;
			}
		}
		return 0;
	}

	/**
	 * 重新设置队长
	 * @param oldLeaderRoleId 旧队长
	 * @param newLeaderRoleId 新队长
	 * @return
	 */
	public static int onSetTeamLeader(long oldLeaderRoleId, long newLeaderRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(oldLeaderRoleId) == false) {
				MessageMgr.sendMsgNotify(oldLeaderRoleId, 141618, null);
				return -20;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(oldLeaderRoleId) == false) {
				MessageMgr.sendMsgNotify(oldLeaderRoleId, 141618, null);
				return -30;
			}
		}
		return 0;
	}

	/**
	 * 回答是否接受队长任命
	 * @param oldLeaderRoleId 旧队长
	 * @param newLeaderRoleId 新队长
	 * @return
	 */
	public static int onAnswerforSetLeader(long oldLeaderRoleId, long newLeaderRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			// 在匹配状态不能更换队长
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(oldLeaderRoleId) == false) {
				MessageMgr.sendMsgNotify(oldLeaderRoleId, 141618, null);
				return -20;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			// 在匹配状态不能更换队长
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(oldLeaderRoleId) == false) {
				MessageMgr.sendMsgNotify(oldLeaderRoleId, 141618, null);
				return -30;
			}
		}
		return 0;
	}

	/**
	 * 请离队员
	 * @param leaderRoleId 队长
	 * @param expeledRoleId 被请离者
	 * @return
	 */
	public static int onExpelMember(long leaderRoleId, long expeledRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leaderRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 141618, null);
				return -20;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leaderRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 141618, null);
				return -30;
			}
		}
		return 0;
	}

	/**
	 * 邀请成员
	 * @param inviterRoleId 邀请者
	 * @param invitedRoleId 被邀请者
	 * @return
	 */
	public static int onInviteJoinTeam(long inviterRoleId, long invitedRoleId) {

		// PVP1
		// 活动期间不能组队
		if (PvP1Control.getInstance().isActivityTime()) {
			final fire.pb.map.Role leaderMapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(inviterRoleId);
			if (PvP1Helper.isPvPMap(leaderMapRole.getMapId())) {
				MessageMgr.sendMsgNotify(inviterRoleId, 143363, null);
				return -10;
			}
			final fire.pb.map.Role memberMapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(invitedRoleId);
			if (memberMapRole == null // 邀请的玩家下线了
					|| PvP1Helper.isPvPMap(memberMapRole.getMapId())) {
				MessageMgr.sendMsgNotify(inviterRoleId, 141619, null);
				return -11;
			}
		}
		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3Helper.isCanJoinPvPMapTeam(inviterRoleId, invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 160607, null);
				return -20;
			}
			// 自己在匹配中不能邀请别人
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(inviterRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 141618, null);
				return -21;
			}
			// 对方在匹配中不能被邀请
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 162057, null);
				return -22;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5Helper.isCanJoinPvPMapTeam(inviterRoleId, invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 160607, null);
				return -30;
			}
			// 自己在匹配中不能邀请别人
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(inviterRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 141618, null);
				return -31;
			}
			// 对方在匹配中不能被邀请
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(inviterRoleId, 162057, null);
				return -32;
			}
		}
		return 0;
	}

	/**
	 * 回应邀请
	 * @param inviterRoleId 邀请者
	 * @param invitedRoleId 被邀请者
	 * @return
	 */
	public static int onRespondInvite(long inviterRoleId, long invitedRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			// 
			if (PvP3Helper.isCanJoinPvPMapTeam(inviterRoleId, invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 160607, null);
				return -20;
			}
			// 邀请者在匹配中不能进入他的队伍
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(inviterRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 162057, null);
				return -21;
			}
			// 自己在匹配中不能同意加入别人队伍
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 141618, null);
				return -22;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			// 
			if (PvP5Helper.isCanJoinPvPMapTeam(inviterRoleId, invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 160607, null);
				return -30;
			}
			// 邀请者在匹配中不能进入他的队伍
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(inviterRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 162057, null);
				return -31;
			}
			// 自己在匹配中不能同意加入别人队伍
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(invitedRoleId) == false) {
				MessageMgr.sendMsgNotify(invitedRoleId, 141618, null);
				return -32;
			}
		}
		return 0;
	}

	/**
	 * 申请加入别人队伍
	 * @param targetRoleId 目标
	 * @param selfRoleId 自己
	 * @return
	 */
	public static int onRequestJoinTeam(long targetRoleId, long selfRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3Helper.isCanJoinPvPMapTeam(targetRoleId, selfRoleId) == false) {
				MessageMgr.sendMsgNotify(selfRoleId, 160607, null);
				return -20;
			}
			// 目标在匹配中不能进入他的队伍
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(targetRoleId) == false) {
				MessageMgr.sendMsgNotify(selfRoleId, 162057, null);
				return -21;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5Helper.isCanJoinPvPMapTeam(targetRoleId, selfRoleId) == false) {
				MessageMgr.sendMsgNotify(selfRoleId, 160607, null);
				return -30;
			}
			// 目标在匹配中不能进入他的队伍
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(targetRoleId) == false) {
				MessageMgr.sendMsgNotify(selfRoleId, 162057, null);
				return -31;
			}
		}
		return 0;
	}

	/**
	 * 队长同意或拒绝申请者加入队伍
	 * @param leaderRoleId 队长
	 * @param applierRoleId 申请者
	 * @return
	 */
	public static int onAcceptToTeam(long leaderRoleId, long applierRoleId) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (PvP3Helper.isCanJoinPvPMapTeam(leaderRoleId, applierRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 160607, null);
				return -20;
			}
			// 队长在匹配状态下不能接收成员
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leaderRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 141618, null);
				return -21;
			}
			// 被接收的成员在匹配状态下不能被接收
			if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(applierRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 162057, null);
				return -22;
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (PvP5Helper.isCanJoinPvPMapTeam(leaderRoleId, applierRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 160607, null);
				return -30;
			}
			// 队长在匹配状态下不能接收成员
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leaderRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 141618, null);
				return -31;
			}
			// 被接收的成员在匹配状态下不能被接收
			if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(applierRoleId) == false) {
				MessageMgr.sendMsgNotify(leaderRoleId, 162057, null);
				return -32;
			}
		}
		return 0;
	}

	/**
	 * 退出队伍
	 * @param leverRoleId 离开者
	 * @param isLeader 是不是队长
	 * @return
	 */
	public static int onQuitTeamProc(long leverRoleId, boolean isLeader) {

		// PVP3
		if (PvP3Control.getInstance().isActivityTime()) {
			if (isLeader) {
				// 在匹配中队长不能离队
				if (PvP3RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leverRoleId) == false) {
					MessageMgr.sendMsgNotify(leverRoleId, 141618, null);
					return -20;
				}
			} else {
				// 响应队伍成员离开
				PvP3Control.getInstance().onTeamMemberLeave(leverRoleId);
			}
		}
		// PVP5
		if (PvP5Control.getInstance().isActivityTime()) {
			if (isLeader) {
				// 在匹配中队长不能离队
				if (PvP5RoleStateManager.getInstance().checkPvPStateCanOperationTeam(leverRoleId) == false) {
					MessageMgr.sendMsgNotify(leverRoleId, 141618, null);
					return -30;
				}
			} else {
				// 响应队伍成员离开
				PvP5Control.getInstance().onTeamMemberLeave(leverRoleId);
			}
		}
		return 0;
	}

	/**
	 * 请求自动匹配
	 * @param roleId 请求者
	 * @return
	 */
	public static int onRequestTeamMatch(long roleId) {

		int roleLevel = xtable.Properties.selectLevel(roleId);

		// PVP1
		// 活动期间不能组队
		if (roleLevel >= PvP1Helper.ENTER_MIN_LEVEL && PvP1Control.getInstance().isActivityTime()) {
			final fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
			// 该场景内不能组队
			if (PvP1Helper.isPvPMap(mapRole.getMapId())) {
				MessageMgr.sendMsgNotify(roleId, 143363, null);
				return -10;
			}
		}
		// PVP3
		if (roleLevel >= PvP3Helper.ENTER_MIN_LEVEL && PvP3Control.getInstance().isActivityTime()) {
			// TODO
		}
		// PVP5
		if (roleLevel >= PvP5Helper.ENTER_MIN_LEVEL && PvP5Control.getInstance().isActivityTime()) {
			// TODO
		}
		return 0;
	}
}
