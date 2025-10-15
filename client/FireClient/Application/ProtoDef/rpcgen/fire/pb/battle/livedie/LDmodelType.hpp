
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 
namespace livedie { 

class LDmodelType : public FireNet::Marshal
{
public:
	enum {
		DAY_FIGHT = 1, // 本日生死战排行
		WEEK_FIGHT = 2, // 本周生死战排行
		HIS_FIGHT = 3, // 历史生死战排行
		SELF_FIGHT = 4, // 自己
	};

	LDmodelType() {
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

	bool operator<(const LDmodelType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const LDmodelType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
} 
