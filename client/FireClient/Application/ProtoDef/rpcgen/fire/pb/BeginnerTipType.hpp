
#pragma once


namespace fire { 
namespace pb { 

class BeginnerTipType : public FireNet::Marshal
{
public:
	enum {
		StudyExtendSkill = 0, // 学习杂学技能
		GotoSchool = 1, // 回职业
		ReleaseApprenticeInfo = 2, // 发布徒弟信息
		ReleaseMasterInfo = 3, // 发布师傅信息
		UseShilizhengming = 4, // 使用实力证明
		BattleSkill = 30001,
		LevelUp = 30002,
		AllocateProperty = 30003,
		AutoFindPath = 30004,
		UseSkill = 30005,
		MiniMap = 30006,
	};

	BeginnerTipType() {
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

	bool operator<(const BeginnerTipType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BeginnerTipType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
