
#pragma once


namespace fire { 
namespace pb { 
namespace mission { 

class FairylandStatus : public FireNet::Marshal
{
public:
	enum {
		LANDOUT = 1,
		LANDIN = 2,
		LANDABSENT = 3,
	};

	FairylandStatus() {
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

	bool operator<(const FairylandStatus &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const FairylandStatus &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
