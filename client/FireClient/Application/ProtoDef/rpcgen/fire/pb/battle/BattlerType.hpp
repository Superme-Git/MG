
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class BattlerType : public FireNet::Marshal
{
public:
	enum {
		BATTLER_CHARACTER = 1, // 人物
		BATTLER_PET = 2, // 宠物
		BATTLER_MONSTER = 3, // 怪物
	};

	BattlerType() {
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

	bool operator<(const BattlerType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BattlerType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
