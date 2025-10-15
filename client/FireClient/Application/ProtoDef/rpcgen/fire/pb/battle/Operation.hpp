
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class Operation : public FireNet::Marshal
{
public:
	int operationtype; // 操作类型	 参考OperationType中的值
	int aim; // 作用目标
	int operationid; // 操作的值（使用物品时表示物品ID，招唤宠物时为宠物）

	Operation() {
		operationtype = 0;
		aim = 0;
		operationid = 0;
	}

	Operation(int _operationtype_, int _aim_, int _operationid_)
		: operationtype(_operationtype_), aim(_aim_), operationid(_operationid_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << operationtype;
		_os_ << aim;
		_os_ << operationid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> operationtype;
		_os_ >> aim;
		_os_ >> operationid;
		return _os_;
	}

	bool operator<(const Operation &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const Operation &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = operationtype - _o_.operationtype;
		if (0 != _c_) return _c_;
		_c_ = aim - _o_.aim;
		if (0 != _c_) return _c_;
		_c_ = operationid - _o_.operationid;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
