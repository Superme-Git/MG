
#pragma once

#include "fire/pb/battle/DemoAttr.hpp"
#include "fire/pb/battle/DemoBuff.hpp"
#include <list>

namespace fire { 
namespace pb { 
namespace battle { 

class DemoResult : public FireNet::Marshal
{
public:
	int resulttype; // 结果类型 0普通 1反击 2连击 3追击 4溅射 5战斗结束 6破隐形
	int targetid; // 受击者id
	int flagtype; // 服务器提供客户端标示
	int hpchange; // 受击者血量变化，为正是加血，为负是扣血
	int mpchange; // 受击者魔法变化，为正是加蓝，为负是扣蓝
	int spchange; // 受击者怒气变化，为正是加怒气，为负是扣怒气
	int epchange; // 受击者效果点变化，为正是加效果点，为负是扣效果点
	int shapechange; // 模型改变 0为默认其他为新设置的。
	int uplimithpchange; // 受击者当前血上限变化，为正是加，为负是减
	int targetresult; // 受击者结果类型，ResultType型枚举值叠加
	int returnhurt; // 受击方造成的反伤值，如果为0则代表没有反伤
	int attackback; // 受击方造成的反击值，如果为0则代表没有反击
	int stealhp; // 攻击方产生的吸血值，如果为0则代表没有吸血
	int attackerresult; // 攻击者结果类型，ResultType型枚举值叠加
	int attackerulhpchange; // 攻击者血上限变化
	int protecterid; // 保护者ID
	int protecterhpchange; // 保护者血量变化，为正是加血，为负是扣血（显然是为负的）
	int protecterulhpchange; // 保护者血上限变化
	int protecterresult; // 保护者结果类型，ResultType型枚举值叠加
	int assisterid; // 合击者ID
	int stealmp; // 攻击方产生的吸蓝值，如果为0则代表没有吸蓝
	int godblesshp; // 神佑血量变化
	std::list<fire::pb::battle::DemoBuff> demobuffs; // 每一个demo的buff变化
	std::list<fire::pb::battle::DemoAttr> demoattrs; // 每一个demo的属性最终值

	DemoResult() {
		resulttype = 0;
		targetid = 0;
		flagtype = 0;
		hpchange = 0;
		mpchange = 0;
		spchange = 0;
		epchange = 0;
		shapechange = 0;
		uplimithpchange = 0;
		targetresult = 0;
		returnhurt = 0;
		attackback = 0;
		stealhp = 0;
		attackerresult = 0;
		attackerulhpchange = 0;
		protecterid = 0;
		protecterhpchange = 0;
		protecterulhpchange = 0;
		protecterresult = 0;
		assisterid = 0;
		stealmp = 0;
		godblesshp = 0;
	}

	DemoResult(int _resulttype_, int _targetid_, int _flagtype_, int _hpchange_, int _mpchange_, int _spchange_, int _epchange_, int _shapechange_, int _uplimithpchange_, int _targetresult_, int _returnhurt_, int _attackback_, int _stealhp_, int _attackerresult_, int _attackerulhpchange_, int _protecterid_, int _protecterhpchange_, int _protecterulhpchange_, int _protecterresult_, int _assisterid_, int _stealmp_, int _godblesshp_, const std::list<fire::pb::battle::DemoBuff> & _demobuffs_, const std::list<fire::pb::battle::DemoAttr> & _demoattrs_)
		: resulttype(_resulttype_), targetid(_targetid_), flagtype(_flagtype_), hpchange(_hpchange_), mpchange(_mpchange_), spchange(_spchange_), epchange(_epchange_), shapechange(_shapechange_), uplimithpchange(_uplimithpchange_), targetresult(_targetresult_), returnhurt(_returnhurt_), attackback(_attackback_), stealhp(_stealhp_), attackerresult(_attackerresult_), attackerulhpchange(_attackerulhpchange_), protecterid(_protecterid_), protecterhpchange(_protecterhpchange_), protecterulhpchange(_protecterulhpchange_), protecterresult(_protecterresult_), assisterid(_assisterid_), stealmp(_stealmp_), godblesshp(_godblesshp_), demobuffs(_demobuffs_), demoattrs(_demoattrs_) {
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
		_os_ << hpchange;
		_os_ << mpchange;
		_os_ << spchange;
		_os_ << epchange;
		_os_ << shapechange;
		_os_ << uplimithpchange;
		_os_ << targetresult;
		_os_ << returnhurt;
		_os_ << attackback;
		_os_ << stealhp;
		_os_ << attackerresult;
		_os_ << attackerulhpchange;
		_os_ << protecterid;
		_os_ << protecterhpchange;
		_os_ << protecterulhpchange;
		_os_ << protecterresult;
		_os_ << assisterid;
		_os_ << stealmp;
		_os_ << godblesshp;
		_os_ << FireNet::MarshalContainer(demobuffs);
		_os_ << FireNet::MarshalContainer(demoattrs);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> resulttype;
		_os_ >> targetid;
		_os_ >> flagtype;
		_os_ >> hpchange;
		_os_ >> mpchange;
		_os_ >> spchange;
		_os_ >> epchange;
		_os_ >> shapechange;
		_os_ >> uplimithpchange;
		_os_ >> targetresult;
		_os_ >> returnhurt;
		_os_ >> attackback;
		_os_ >> stealhp;
		_os_ >> attackerresult;
		_os_ >> attackerulhpchange;
		_os_ >> protecterid;
		_os_ >> protecterhpchange;
		_os_ >> protecterulhpchange;
		_os_ >> protecterresult;
		_os_ >> assisterid;
		_os_ >> stealmp;
		_os_ >> godblesshp;
		_os_ >> FireNet::MarshalContainer(demobuffs);
		_os_ >> FireNet::MarshalContainer(demoattrs);
		return _os_;
	}

};

} 
} 
} 
