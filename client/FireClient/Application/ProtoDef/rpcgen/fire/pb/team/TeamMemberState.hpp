
#pragma once


namespace fire { 
namespace pb { 
namespace team { 

class TeamMemberState : public FireNet::Marshal
{
public:
	enum {
		eTeamNormal = 1, // Õı³£×´Ì¬
		eTeamAbsent = 2, // ÔİÀë×´Ì¬
		eTeamReturn = 3, // »Ø¹é¶ÓÎé×´Ì¬
		eTeamFallline = 4, // ÀëÏß×´Ì¬
	};

	TeamMemberState() {
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

	bool operator<(const TeamMemberState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const TeamMemberState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
