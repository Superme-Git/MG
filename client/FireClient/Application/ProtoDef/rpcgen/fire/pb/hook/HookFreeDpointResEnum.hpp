
#pragma once


namespace fire { 
namespace pb { 
namespace hook { 

class HookFreeDpointResEnum : public FireNet::Marshal
{
public:
	enum {
		FREEDPOINTSUCC = 0, // 领取成功
		FREEDPOINTFAIL = 1, // 领取失败
	};

	HookFreeDpointResEnum() {
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

	bool operator<(const HookFreeDpointResEnum &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const HookFreeDpointResEnum &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
