
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamError implements Marshal , Comparable<TeamError>{
	public final static int UnkownError = 0; // 未知错误
	public final static int SelfInTeam = 1; // 自己已经在队伍中
	public final static int SelfNotInTeam = 2; // 自己不在队伍中
	public final static int ObjectInTeam = 3; // 对方已经在队伍中
	public final static int SelfNOtLeader = 4; // 自己不是队长
	public final static int ObjectNotLeader = 5; // 对方不是队长
	public final static int ObjectOffline = 6; // 对方不在线（有统一的Error吗）
	public final static int SelfTeamFunctionClose = 7; // 自己组队开关关闭
	public final static int ObjectTeamFunctionClose = 8; // 对方组队开关关闭
	public final static int SelfInUnteamState = 9; // 自己在不可组队状态
	public final static int ObjectInUnteamState = 10; // 对方在不可组队状态
	public final static int TeamFull = 11; // 队伍人数已满
	public final static int InvitedInTeam = 12; // 对方已经在队伍中
	public final static int BeingInvited = 13; // 对方正在被其他人邀请中
	public final static int InvitedIn30s = 14; // 30秒内只能邀请一次同一玩家
	public final static int InviteingsFull = 15; // 正在邀请人数达到4个，不能再邀请更多
	public final static int InviterTeamNotExist = 16; // 邀请您的队伍已经解散
	public final static int InviterNotLeader = 17; // 邀请者不是队长
	public final static int ApplierInTeam = 18; // 申请者已经在队伍中
	public final static int ApplyTimeout = 19; // 该申请已经超时
	public final static int ApplyListFull = 20; // 队伍申请列表已满
	public final static int ApplierLevelValid = 21; // 申请者级别不符合队伍要求
	public final static int ChangeLeaderUnable = 22; // 队伍处在不可以换队长的状态
	public final static int InChangeLeaderStatus = 23; // 已经提出更换队长，等待回应中
	public final static int ChangeLeaderInCD = 24; // 队伍2分钟只能更换队长一次
	public final static int MembersNotNormal = 25; // 队员不处于正常状态
	public final static int TooFar = 26; // 距离过远，不能归队
	public final static int NoAbsentMember = 27; // 队伍没有暂离的队员
	public final static int RefuseChangeLeader = 28; // 拒绝成为队长
	public final static int ObjectNotInTeam = 29; // 对方不在队伍中
	public final static int AlreadyApply = 30; // 对方已经在此队伍的申请列表中
	public final static int AbsentCantBeLeader = 31; // 暂离队员不能成为队长
	public final static int LevelSetError = 32; // 等级设置错误 by changhao
	public final static int LevelError = 33; // 等级不符合 by changhao
	public final static int NoTarget = 34; // 没有设置目标 by changhao
	public final static int TeamEnoughFull = 35; // 队伍已经组满 by changhao
	public final static int InMatching = 36; // 已经在匹配中 by changhao
	public final static int ActiveNotOpen = 37; // 活动未开放 by changhao
	public final static int NoFaction = 38; // 没有工会 by changhao
	public final static int TeamStateError = 39; // 组队状态客户端服务器不同步 by changhao
	public final static int OneKeyApplyTeamNoTime = 40; // 一键喊话不到时间 by changhao
	public final static int NoRollNotInTeam = 50; // 不在队伍中不能ROLL点 by changhao
	public final static int NoReward = 51; // 没有奖励可分配 by changhao
	public final static int FormBookHalfNotEnough = 55; // 光环残卷不足 by changhao
	public final static int UnKnuownFormBook = 56; // 无效的光环书 by changhao
	public final static int FromLevelMax = 57; // 光环等级已经最高了 by changhao
	public final static int FormIdError = 58; // 光环ID 错误 by changhao
	public final static int FormBookNotEnough = 59; // 光环书不足 by changhao


	public TeamError() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamError) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TeamError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

