
#pragma once

#include "fire/pb/move/Pos.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class PickUpItem : public FireNet::Marshal
{
public:
	int64_t uniqueid; // 物品的实例ID
	int baseid; // 物品的模板ID
	fire::pb::move::Pos pos; // 位置

	PickUpItem() {
		uniqueid = 0;
		baseid = 0;
	}

	PickUpItem(int64_t _uniqueid_, int _baseid_, const fire::pb::move::Pos & _pos_)
		: uniqueid(_uniqueid_), baseid(_baseid_), pos(_pos_) {
	}

	bool _validator_() const {
		if (!pos._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << uniqueid;
		_os_ << baseid;
		_os_ << pos;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> uniqueid;
		_os_ >> baseid;
		_os_ >> pos;
		return _os_;
	}

	bool operator<(const PickUpItem &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PickUpItem &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = FireNet::signum(uniqueid - _o_.uniqueid);
		if (0 != _c_) return _c_;
		_c_ = baseid - _o_.baseid;
		if (0 != _c_) return _c_;
		_c_ = pos.compareTo(_o_.pos);
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
