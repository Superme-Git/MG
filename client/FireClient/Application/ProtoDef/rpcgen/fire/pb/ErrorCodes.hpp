
#pragma once


namespace fire { 
namespace pb { 

class ErrorCodes : public FireNet::Marshal
{
public:
	enum {
		NoError = 0, // 没有错误
		AddItemToBagException = 1, // 往背包添加物品出错
		NotEnoughMoney = 2, // 金钱不足
		EquipPosNotSuit = 3, // 装备位置不符合
		EquipLevelNotSuit = 4, // 装备等级不符合
		EquipSexNotSuit = 5, // 装备性别不符合
	};

	ErrorCodes() {
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

	bool operator<(const ErrorCodes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ErrorCodes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
