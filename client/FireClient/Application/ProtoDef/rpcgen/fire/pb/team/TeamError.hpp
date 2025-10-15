
#pragma once


namespace fire { 
namespace pb { 
namespace team { 

class TeamError : public FireNet::Marshal
{
public:
	enum {
		UnkownError = 0, // 未知错误
		SelfInTeam = 1, // 自己已经在队伍中
		SelfNotInTeam = 2, // 自己不在队伍中
		ObjectInTeam = 3, // 对方已经在队伍中
		SelfNOtLeader = 4, // 自己不是队长
		ObjectNotLeader = 5, // 对方不是队长
		ObjectOffline = 6, // 对方不在线（有统一的Error吗）
		SelfTeamFunctionClose = 7, // 自己组队开关关闭
		ObjectTeamFunctionClose = 8, // 对方组队开关关闭
		SelfInUnteamState = 9, // 自己在不可组队状态
		ObjectInUnteamState = 10, // 对方在不可组队状态
		TeamFull = 11, // 队伍人数已满
		InvitedInTeam = 12, // 对方已经在队伍中
		BeingInvited = 13, // 对方正在被其他人邀请中
		InvitedIn30s = 14, // 30秒内只能邀请一次同一玩家
		InviteingsFull = 15, // 正在邀请人数达到4个，不能再邀请更多
		InviterTeamNotExist = 16, // 邀请您的队伍已经解散
		InviterNotLeader = 17, // 邀请者不是队长
		ApplierInTeam = 18, // 申请者已经在队伍中
		ApplyTimeout = 19, // 该申请已经超时
		ApplyListFull = 20, // 队伍申请列表已满
		ApplierLevelValid = 21, // 申请者级别不符合队伍要求
		ChangeLeaderUnable = 22, // 队伍处在不可以换队长的状态
		InChangeLeaderStatus = 23, // 已经提出更换队长，等待回应中
		ChangeLeaderInCD = 24, // 队伍2分钟只能更换队长一次
		MembersNotNormal = 25, // 队员不处于正常状态
		TooFar = 26, // 距离过远，不能归队
		NoAbsentMember = 27, // 队伍没有暂离的队员
		RefuseChangeLeader = 28, // 拒绝成为队长
		ObjectNotInTeam = 29, // 对方不在队伍中
		AlreadyApply = 30, // 对方已经在此队伍的申请列表中
		AbsentCantBeLeader = 31, // 暂离队员不能成为队长
		LevelSetError = 32, // 等级设置错误 by changhao
		LevelError = 33, // 等级不符合 by changhao
		NoTarget = 34, // 没有设置目标 by changhao
		TeamEnoughFull = 35, // 队伍已经组满 by changhao
		InMatching = 36, // 已经在匹配中 by changhao
		ActiveNotOpen = 37, // 活动未开放 by changhao
		NoFaction = 38, // 没有工会 by changhao
		TeamStateError = 39, // 组队状态客户端服务器不同步 by changhao
		OneKeyApplyTeamNoTime = 40, // 一键喊话不到时间 by changhao
		NoRollNotInTeam = 50, // 不在队伍中不能ROLL点 by changhao
		NoReward = 51, // 没有奖励可分配 by changhao
		FormBookHalfNotEnough = 55, // 光环残卷不足 by changhao
		UnKnuownFormBook = 56, // 无效的光环书 by changhao
		FromLevelMax = 57, // 光环等级已经最高了 by changhao
		FormIdError = 58, // 光环ID 错误 by changhao
		FormBookNotEnough = 59, // 光环书不足 by changhao
	};

	TeamError() {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	bool operator<(const TeamError &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const TeamError &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
