
#pragma once


namespace fire { 
namespace pb { 
namespace item { 

class EquipItemType : public FireNet::Marshal
{
public:
	enum {
		ARMS = 0, // //武器
		ADORN = 2, // //项链
		LORICAE = 3, // //衣服
		WAISTBAND = 4, // //腰带
		BOOT = 5, // //靴子
		TIRE = 6, // //帽子
		ROLE_COLOR1 = 50, // 染色1
		ROLE_COLOR2 = 51, // 染色2
		EQUIP_EFFECT = 60, // 装备特效
	};

	EquipItemType() {
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

	bool operator<(const EquipItemType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const EquipItemType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
