
#pragma once


namespace fire { 
namespace pb { 
namespace move { 

class Pos : public FireNet::Marshal
{
public:
	short x; // x
	short y; // y

	Pos() {
		x = 0;
		y = 0;
	}

	Pos(short _x_, short _y_)
		: x(_x_), y(_y_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << x;
		_os_ << y;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> x;
		_os_ >> y;
		return _os_;
	}

	bool operator<(const Pos &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const Pos &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = x - _o_.x;
		if (0 != _c_) return _c_;
		_c_ = y - _o_.y;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
