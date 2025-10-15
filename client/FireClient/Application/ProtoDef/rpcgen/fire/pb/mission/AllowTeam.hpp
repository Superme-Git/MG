
#pragma once


namespace fire { 
namespace pb { 
namespace mission { 

class AllowTeam : public FireNet::Marshal
{
public:
	enum {
		NO = 0,
		YES = 1,
		MUST = 2,
	};

	AllowTeam() {
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

	bool operator<(const AllowTeam &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AllowTeam &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
