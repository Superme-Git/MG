
#pragma once


namespace fire { 
namespace pb { 

class KeyCounterIndex : public FireNet::Marshal
{
public:
	enum {
		FACTION_KEY = 1000, // 公会
		FAMILY_KEY = 2000,
		ITEM_KEY = 3000, // 物品
	};

	KeyCounterIndex() {
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

	bool operator<(const KeyCounterIndex &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const KeyCounterIndex &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
