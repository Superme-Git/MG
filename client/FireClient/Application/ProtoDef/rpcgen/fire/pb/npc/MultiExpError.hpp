
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class MultiExpError : public FireNet::Marshal
{
public:
	enum {
		LevelConditionErr = -2, // 等级不够
		ExistOtherMultiExpBuff = -3, // 存在其他多倍经验的buff，不能领取别的。
		NoOneMultiExpBuff = -4, // 不存在多倍经验的buff，不能退还
		NoHaveBuffTime = -5, // 没有可以领取的时间，不能领取
		OverMaxHours = -6, // 身上多倍时间的buff大于4小时
	};

	MultiExpError() {
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

	bool operator<(const MultiExpError &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MultiExpError &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
