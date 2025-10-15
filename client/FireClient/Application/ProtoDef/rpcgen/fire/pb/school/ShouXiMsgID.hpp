
#pragma once


namespace fire { 
namespace pb { 
namespace school { 

class ShouXiMsgID : public FireNet::Marshal
{
public:
	enum {
		ChallengeLevelNotEnough = 140245, // 挑战首席-弟子是很危险的，40级以后再来挑战吧！”
		SchoolContriNotEnough = 140246, // 你的职业贡献度不足500点，无法发起挑战！
		InTeam = 140247, // 请离开队伍再来参加挑战吧！
		MoneyNotEnough = 140248, // 你身上的金钱好像不足10000两啊！
		ChallengeSuccess = 140249, // 挑战成功，你可以参与职业首席-弟子竞选了！
		ChallengeLost = 140250, // 挑战失败，希望你下次再接再厉!
		NotSuccess = 140251, // 您还未成功挑战首席-弟子，不能参加竞选。
		AlreadyCandidate = 140252, // 您已成功竞选！
		VoteLevelNotEnough = 140253, // 等级≥30级的玩家方可参与投票哦！
		TiLiNotEnough = 140254, // 您的体力不足100点，不能投票！
		AlreadyVote = 140255, // 您本周内已经投过票，不能再投了！
		GiveShouXiTitle = 140256, // 恭喜你获得了首席-弟子称号。
		CancelShouXiTitle = 140257, // 很抱歉，你已不再是首席-弟子。
		RefreshAbilityToMuch = 140258, // 休息一下，明天再来吧！
		ChallengeAffirm = 140259, // 挑战首席-弟子需要花费10000两金钱，是否确认挑战？
		Cantvote = 140260, // 现在不是投票时间哦。
		CantChallenge = 140261, // 现在不是挑战时间哦。
	};

	ShouXiMsgID() {
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

	bool operator<(const ShouXiMsgID &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ShouXiMsgID &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
