
#pragma once


namespace fire { 
namespace pb { 
namespace shop { 

class GoodsType : public FireNet::Marshal
{
public:
	enum {
		NORMAL_ITEM = 1, // 道具
		PET = 2, // 宠物
		EQUIP_ITEM = 3, // 装备
		OTHER_ITEM = 4, // 特殊类型(用于标示价格浮动特殊处理)
	};

	GoodsType() {
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

	bool operator<(const GoodsType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GoodsType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
