
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class NpcBuyType : public FireNet::Marshal
{
public:
	enum {
		COMMON_MONEY_BUY = 0,
		COMMON_SMONEY_BUY = 1,
		FAMILY_SELL = 2,
		SKILL_SELL = 3,
		BUY_AND_USE = 6, // 用符石购买物品，购买后直接使用，如果购买多个，扣除一个后其余的放入背包
		MONEY_BUY_AND_USE = 7, // 用现金购买物品，购买后直接使用
	};

	NpcBuyType() {
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

	bool operator<(const NpcBuyType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const NpcBuyType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
