
#pragma once


namespace fire { 
namespace pb { 
namespace skill { 

class EquipSkill : public FireNet::Marshal
{
public:
	int skill; // 特技id
	int effect; // 特效id

	EquipSkill() {
		skill = 0;
		effect = 0;
	}

	EquipSkill(int _skill_, int _effect_)
		: skill(_skill_), effect(_effect_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << skill;
		_os_ << effect;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> skill;
		_os_ >> effect;
		return _os_;
	}

	bool operator<(const EquipSkill &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const EquipSkill &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skill - _o_.skill;
		if (0 != _c_) return _c_;
		_c_ = effect - _o_.effect;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
