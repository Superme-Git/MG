
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class SubmitType : public FireNet::Marshal
{
public:
	enum {
		ITEM = 1, // 给予npc，任务相关的物品
		PET = 2, // 给予npc,任务相关的宠物
		MONEY = 3, // 给予npc,任务相关的金钱
		FACTION_MONEY_BOX = 4, // 给予npc,公会银箱
		FAMILY_MONEY_BOX = 5, // 给予npc,银箱
		INSTANCE_ZONE_ITEM = 13, // 副本玩法内的提交物品
		GUILD_ZONE_ITEM = 22, // 副本玩法内的提交物品
	};

	SubmitType() {
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

	bool operator<(const SubmitType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SubmitType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
