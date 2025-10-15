
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class BattleAimRelation : public FireNet::Marshal
{
public:
	enum {
		SELF = 1, // 战斗中自己（可以是人、宠、怪、NPC）
		SELF_PET = 2, // 自己的宠物
		FRIEND_ROLE = 4, // 友方角色
		FRIEND_PET = 8, // 友方的宠物
		FRIEND_NPC = 16, // 友方NPC
		ENERMY_ROLE = 32, // 敌人角色
		ENERMY_PET = 64, // 敌人宠物
		ENERMY_NPC = 128, // 敌人NPC
		ENERMY_MONSTER = 256, // 敌人怪物
		COUPLE = 512, // 夫妻
		BROTHERS = 1024, // 结交
		MASTER_STUDENT = 2048, // 师徒
		FRIEND_MONSTER = 4096, // 友方怪物
	};

	BattleAimRelation() {
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

	bool operator<(const BattleAimRelation &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BattleAimRelation &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
