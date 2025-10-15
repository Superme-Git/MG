
#pragma once


namespace fire { 
namespace pb { 

class PingStatEntry : public FireNet::Marshal
{
public:
	short _max;
	short _min;
	short _avg;

	PingStatEntry() {
		_max = 0;
		_min = 0;
		_avg = 0;
	}

	PingStatEntry(short __max_, short __min_, short __avg_)
		: _max(__max_), _min(__min_), _avg(__avg_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << _max;
		_os_ << _min;
		_os_ << _avg;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> _max;
		_os_ >> _min;
		_os_ >> _avg;
		return _os_;
	}

	bool operator<(const PingStatEntry &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PingStatEntry &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = _max - _o_._max;
		if (0 != _c_) return _c_;
		_c_ = _min - _o_._min;
		if (0 != _c_) return _c_;
		_c_ = _avg - _o_._avg;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
