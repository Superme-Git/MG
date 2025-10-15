
#pragma once


namespace fire { 
namespace pb { 
namespace fushi { 
namespace redpack { 

class RedPackState : public FireNet::Marshal
{
public:
	enum {
		STATE_CANGET = 0, // 可以抢红包
		STATE_HAVE = 1, // 已经领取红包
		STATE_NONE = 2, // 红包已经抢光
	};

	RedPackState() {
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

	bool operator<(const RedPackState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RedPackState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
} 
