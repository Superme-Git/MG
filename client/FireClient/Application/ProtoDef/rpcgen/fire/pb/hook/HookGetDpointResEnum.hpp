
#pragma once


namespace fire { 
namespace pb { 
namespace hook { 

class HookGetDpointResEnum : public FireNet::Marshal
{
public:
	enum {
		GETDPOINTSUCC = 0, // 领取成功
		GETDPOINTLIMIT = 1, // 到达领取上限，失败
		GETDPOINTFAIL = 2, // 其他失败原因
	};

	HookGetDpointResEnum() {
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

	bool operator<(const HookGetDpointResEnum &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const HookGetDpointResEnum &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
