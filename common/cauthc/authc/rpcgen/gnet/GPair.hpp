
#pragma once


namespace gnet { 

class GPair : public FireNet::Marshal
{
public:
	int key;
	int value;

	GPair() {
		key = 0;
		value = 0;
	}

	GPair(int _key_, int _value_)
		: key(_key_), value(_value_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << key;
		_os_ << value;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> key;
		_os_ >> value;
		return _os_;
	}

	bool operator<(const GPair &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GPair &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
