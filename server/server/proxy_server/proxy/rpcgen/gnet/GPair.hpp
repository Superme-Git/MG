
#pragma once


namespace gnet { 

class GPair : public GNET::Rpc::Data
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

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << key;
		_os_ << value;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> key;
		_os_ >> value;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GPair(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GPair *o = dynamic_cast<const GPair*>(&d)) return *this = *o;
		return *this;
	}

	GPair& operator=(const GPair & d)
	{
		if (&d == this) return *this;
		key = d.key;
		value = d.value;
		return *this;
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
