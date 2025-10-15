
#pragma once


namespace fire { 
namespace pb { 
namespace team { 

class TeamState : public FireNet::Marshal
{
public:
	enum {
		eNormalTeam = 1, // 正常状态
		eWaitingSummonTeam = 2, // 等待中的队伍状态
		eSummonTeam = 3, // 召集令队伍状态
	};

	TeamState() {
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

	bool operator<(const TeamState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const TeamState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
