
#pragma once


namespace fire { 
namespace pb { 
namespace school { 

class ShouXiLimit : public FireNet::Marshal
{
public:
	enum {
		CHALLENGE_LEVEL_LIMIT = 40, // 挑战首席的等级限制
		SCHOOL_CONTRIBUTE_LIMIT = 500, // 挑战首席需要的职业贡献度
		MONEY_LIMIT = 10000, // 挑战首席需要的金钱
		VOTE_LEVEL_LIMIT = 30, // 投票需要的等级
		TILI_LIMIT = 100, // 投票需要的体力值
		REFRESH_TIMES_LIMIT = 5, // 投票需要的体力值
	};

	ShouXiLimit() {
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

	bool operator<(const ShouXiLimit &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ShouXiLimit &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
