
#pragma once


namespace fire { 
namespace pb { 
namespace shop { 

class MarketType : public FireNet::Marshal
{
public:
	enum {
		CARE_FOR = 1, // 关注
		RARITY_PET = 2, // 珍品宠物(珍品)
		RARITY_EQUIP = 3, // 珍品装备(珍品)
		RARITY_PET_BOOK = 4, // 高级兽决(珍品)
		NORMAL_PET_BOOK = 5, // 低级兽决
		NORMAL_PHARMACEUTICAL = 6, // 药品
		COOKING = 7, // 烹饪
		ZHI_ZAO_FU = 8, // 制造符
		LIN_SHI_FU = 9, // 临时符
		TASK_ITEM = 10, // 任务道具
		PET_SHOP = 11, // 奇珍异宝
		RARITY_EQUIP_FORGING = 12, // 装备打造(珍品)
	};

	MarketType() {
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

	bool operator<(const MarketType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MarketType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
