
#pragma once


namespace fire { 
namespace pb { 

class RoleState : public FireNet::Marshal
{
public:
	enum {
		OFFLINE = 0, // 离线
		NONAL = 1, // 正常
		LEAVE = 2, // 离开
		BUSY = 3, // 忙碌
	};

	RoleState() {
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

	bool operator<(const RoleState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RoleState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
