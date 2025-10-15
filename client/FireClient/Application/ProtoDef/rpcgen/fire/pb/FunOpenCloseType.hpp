
#pragma once


namespace fire { 
namespace pb { 

class FunOpenCloseType : public FireNet::Marshal
{
public:
	enum {
		FUN_REDPACK = 1, // 红包
		FUN_JHM = 2, // 激活码
		FUN_CHECKPOINT = 3, // 点卡交易所
		FUN_MONTHCARD = 4, // 月卡
		FUN_BLACKMARKET = 5, // 黑市
	};

	FunOpenCloseType() {
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

	bool operator<(const FunOpenCloseType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const FunOpenCloseType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
