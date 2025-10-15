
#pragma once

#include "fire/pb/battle/DemoBuff.hpp"
#include <list>

namespace fire { 
namespace pb { 
namespace battle { 

class DemoExecute : public FireNet::Marshal
{
public:
	int attackerid; // 攻击发起者id
	int hpconsume; // 攻击者耗血，始终为正好了
	int mpconsume; // 攻击者耗蓝，始终为正好了
	int spconsume; // 攻击者耗怒，始终为正好了
	int operationtype; // 操作类型 参考OperationType中的值		如果是操作失败 ACTION_FAILURE
	int operationid; // 操作的值（使用物品时示物品ID，招唤宠物时为宠物）
	int msgid; // 当不为0时，为动作时的提示ID
	std::list<fire::pb::battle::DemoBuff> demobuffs;

	DemoExecute() {
		attackerid = 0;
		hpconsume = 0;
		mpconsume = 0;
		spconsume = 0;
		operationtype = 0;
		operationid = 0;
		msgid = 0;
	}

	DemoExecute(int _attackerid_, int _hpconsume_, int _mpconsume_, int _spconsume_, int _operationtype_, int _operationid_, int _msgid_, const std::list<fire::pb::battle::DemoBuff> & _demobuffs_)
		: attackerid(_attackerid_), hpconsume(_hpconsume_), mpconsume(_mpconsume_), spconsume(_spconsume_), operationtype(_operationtype_), operationid(_operationid_), msgid(_msgid_), demobuffs(_demobuffs_) {
	}

	bool _validator_() const {
		for (std::list<fire::pb::battle::DemoBuff>::const_iterator _i_ = demobuffs.begin(); _i_ != demobuffs.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << attackerid;
		_os_ << hpconsume;
		_os_ << mpconsume;
		_os_ << spconsume;
		_os_ << operationtype;
		_os_ << operationid;
		_os_ << msgid;
		_os_ << FireNet::MarshalContainer(demobuffs);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> attackerid;
		_os_ >> hpconsume;
		_os_ >> mpconsume;
		_os_ >> spconsume;
		_os_ >> operationtype;
		_os_ >> operationid;
		_os_ >> msgid;
		_os_ >> FireNet::MarshalContainer(demobuffs);
		return _os_;
	}

};

} 
} 
} 
