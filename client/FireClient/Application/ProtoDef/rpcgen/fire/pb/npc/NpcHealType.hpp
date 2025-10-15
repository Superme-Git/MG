
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class NpcHealType : public FireNet::Marshal
{
public:
	enum {
		ROLE_RECOVERY = 1,
		PETS_HEALTH_RECOVERY = 2,
		PETS_LOYALTY_RECOVERY = 3,
		PETS_RECOVERY = 4,
	};

	NpcHealType() {
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

	bool operator<(const NpcHealType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const NpcHealType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
