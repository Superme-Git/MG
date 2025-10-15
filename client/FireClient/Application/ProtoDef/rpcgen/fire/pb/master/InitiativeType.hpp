
#pragma once


namespace fire { 
namespace pb { 
namespace master { 

class InitiativeType : public FireNet::Marshal
{
public:
	enum {
		INITIATIVE = 0, // 主动
		PASSIVE = 1, // 被动
		AUTO = 2, // 自动
	};

	InitiativeType() {
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

	bool operator<(const InitiativeType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const InitiativeType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
