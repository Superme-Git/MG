
#pragma once


namespace fire { 
namespace pb { 
namespace attr { 

class ScoreType : public FireNet::Marshal
{
public:
	enum {
		ROLE_LEVEL_SCORE = 1, // 角色等级能力
		EQUIP_LEVEL_SCORE = 2, // 装备一级属性
		EQUIP_EFFCT = 3, // 装备特效
		EQUIP_SKILL = 4, // 装备特技
		EQUIP_GEM = 5, // 每个宝石
		SCHOOL_SKILL = 6, // 职业技能
		GUILD_SKILL = 7, // 公会技能
		GUILD_SHAVE = 8, // 公会修炼
		PET_LEVEL_SCORE = 9, // 宠物一级属性
		PET_LOW_SKILL = 10, // 宠物低级技能
		PET_HIGH_SKILL = 11, // 宠物高级技能
		PET_GROWING = 12, // 宠物成长
	};

	ScoreType() {
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

	bool operator<(const ScoreType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ScoreType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
