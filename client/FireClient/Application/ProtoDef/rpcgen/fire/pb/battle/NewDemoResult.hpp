
#pragma once

#include "fire/pb/battle/DemoAttr.hpp"
#include "fire/pb/battle/DemoBuff.hpp"
#include <list>
#include <map>

namespace fire { 
namespace pb { 
namespace battle { 

class NewDemoResult : public FireNet::Marshal
{
public:
	enum {
		HP_CHANGE = 1, // 受击者血量变化，为正是加血，为负是扣血
		MP_CHANGE = 2, // 受击者魔法变化，为正是加蓝，为负是扣蓝
		SP_CHANGE = 3, // 受击者怒气变化，为正是加怒气，为负是扣怒气
		UL_HP_CHANGE = 4, // 受击者当前血上限变化，为正是加，为负是减
		TARGET_RESULT = 5, // 受击者结果类型，ResultType型枚举值叠加
		RETURN_HURT = 6, // 受击方造成的反伤值，如果为0则代表没有反伤
		ATTACK_BACK = 7, // 受击方造成的反击值，如果为0则代表没有反击
		STEAL_HP = 8, // 攻击方产生的吸血值，如果为0则代表没有吸血
		ATTACKER_RESULT = 9, // 攻击者结果类型，ResultType型枚举值叠加
		PROTECTER_ID = 10, // 保护者ID
		PROTECTER_HP_CHANGE = 11, // 保护者血量变化，为正是加血，为负是扣血（显然是为负的）
		PROTECTER_RESULT = 12, // 保护者结果类型，ResultType型枚举值叠加
		ASSISTER_ID = 13, // 合击者ID
		STEAL_MP = 14, // 攻击方产生的吸蓝值，如果为0则代表没有吸蓝
		RETURN_HURT_DEATH = 15, // 攻击者因为被反伤或反击致死而产生的伤的变化
		PROTECTER_MAXHP_CHANGE = 16, // 保护者因为保护致死而产生的伤的变化
		MESSAGE_ID = 17, // 行动时弹的提示ID
		HP_GODBLESS = 18, // 神佑血量变化
		EP_CHANGE = 19, // 受击者效果点变化，为正是加效果点，为负是扣效果点
		SHAPE_CHANGE = 20, // 模型改变
	};

	int resulttype; // 结果类型 0普通 1反击 2连击 3追击 4溅射 5战斗结束 6破隐形
	int targetid; // 目标id，必须有
	int flagtype; // 服务器提供客户端标示
	std::list<fire::pb::battle::DemoBuff> demobuffs; // 每一个demo的buff变化
	std::list<fire::pb::battle::DemoAttr> demoattrs; // 每一个demo的属性最终值
	std::map<int, int> datas; // 存储数据，key参考以下值，如datas中某key值没有则value默认为0

	NewDemoResult() {
		resulttype = 0;
		targetid = 0;
		flagtype = 0;
	}

	NewDemoResult(int _resulttype_, int _targetid_, int _flagtype_, const std::list<fire::pb::battle::DemoBuff> & _demobuffs_, const std::list<fire::pb::battle::DemoAttr> & _demoattrs_, const std::map<int, int> & _datas_)
		: resulttype(_resulttype_), targetid(_targetid_), flagtype(_flagtype_), demobuffs(_demobuffs_), demoattrs(_demoattrs_), datas(_datas_) {
	}

	bool _validator_() const {
		for (std::list<fire::pb::battle::DemoBuff>::const_iterator _i_ = demobuffs.begin(); _i_ != demobuffs.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		for (std::list<fire::pb::battle::DemoAttr>::const_iterator _i_ = demoattrs.begin(); _i_ != demoattrs.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << resulttype;
		_os_ << targetid;
		_os_ << flagtype;
		_os_ << FireNet::MarshalContainer(demobuffs);
		_os_ << FireNet::MarshalContainer(demoattrs);
		_os_ << FireNet::MarshalContainer(datas);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> resulttype;
		_os_ >> targetid;
		_os_ >> flagtype;
		_os_ >> FireNet::MarshalContainer(demobuffs);
		_os_ >> FireNet::MarshalContainer(demoattrs);
		_os_ >> FireNet::MarshalContainer(datas);
		return _os_;
	}

};

} 
} 
} 
