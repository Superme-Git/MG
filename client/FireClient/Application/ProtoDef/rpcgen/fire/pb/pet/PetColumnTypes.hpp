
#pragma once


namespace fire { 
namespace pb { 
namespace pet { 

class PetColumnTypes : public FireNet::Marshal
{
public:
	enum {
		PET = 1, // 人物身上的宠物栏
		DEPOT = 2, // 人物的仓库宠物栏
		XUNBAOSELLPET = 3, // 找宝网寄售宠物栏
		XUNBAOBUYPET = 4, // 找宝网买入宠物栏
		MARKETPET = 5, // 拍卖宠物栏
		BLACKMARKETPET = 6, // 黑市宠物栏
	};

	PetColumnTypes() {
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

	bool operator<(const PetColumnTypes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PetColumnTypes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
