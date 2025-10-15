
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class DemoAttr : public FireNet::Marshal
{
public:
	int fighterid;
	int attrid; //  Ù–‘id
	int value; // ◊Ó÷’÷µ

	DemoAttr() {
		fighterid = 0;
		attrid = 0;
		value = 0;
	}

	DemoAttr(int _fighterid_, int _attrid_, int _value_)
		: fighterid(_fighterid_), attrid(_attrid_), value(_value_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << fighterid;
		_os_ << attrid;
		_os_ << value;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> fighterid;
		_os_ >> attrid;
		_os_ >> value;
		return _os_;
	}

	bool operator<(const DemoAttr &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const DemoAttr &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterid - _o_.fighterid;
		if (0 != _c_) return _c_;
		_c_ = attrid - _o_.attrid;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
