
#pragma once


namespace fire { 
namespace pb { 
namespace circletask { 

class RewardItemUnit : public FireNet::Marshal
{
public:
	int baseid; // 奖励道具的基本id
	int num; // 奖励道具的数量

	RewardItemUnit() {
		baseid = 0;
		num = 0;
	}

	RewardItemUnit(int _baseid_, int _num_)
		: baseid(_baseid_), num(_num_) {
	}

	bool _validator_() const {
		if (baseid <= 0) return false;
		if (num <= 0) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << baseid;
		_os_ << num;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> baseid;
		_os_ >> num;
		return _os_;
	}

	bool operator<(const RewardItemUnit &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RewardItemUnit &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = baseid - _o_.baseid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
