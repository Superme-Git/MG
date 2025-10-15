
#pragma once


namespace fire { 
namespace pb { 
namespace hook { 

class HookDataUpdateEnum : public FireNet::Marshal
{
public:
	enum {
		HOOKDATA_CANGETDPOINT = 0,
		HOOKDATA_GETDPOINT = 1,
		HOOKDATA_ISAUTOBATTLE = 2,
		HOOKDATA_CHAROPTYPE = 3,
		HOOKDATA_CHAROPID = 4,
		HOOKDATA_PETOPTYPE = 5,
		HOOKDATA_PETOPID = 6,
		HOOKDATA_OFFLINEEXP = 7,
	};

	HookDataUpdateEnum() {
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

	bool operator<(const HookDataUpdateEnum &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const HookDataUpdateEnum &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
