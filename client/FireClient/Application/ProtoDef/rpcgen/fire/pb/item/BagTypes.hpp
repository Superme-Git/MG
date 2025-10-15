
#pragma once


namespace fire { 
namespace pb { 
namespace item { 

class BagTypes : public FireNet::Marshal
{
public:
	enum {
		EMPTY = 0,
		BAG = 1, // 背包
		DEPOT = 2, // 仓库
		EQUIP = 3, // 装备背包
		TEMP = 4, // 临时背包
		QUEST = 5, // 任务背包
		MARKET = 6, // 拍卖背包
		BLACKMARKET = 7, // 黑市背包
	};

	BagTypes() {
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

	bool operator<(const BagTypes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BagTypes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
