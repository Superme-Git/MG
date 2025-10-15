
#pragma once


namespace gnet { 

class SetServerAttrArg : public GNET::Rpc::Data
{
public:
	int gmuserid;
	int localsid;
	int attribute;
	int value;

	SetServerAttrArg() {
		gmuserid = 0;
		localsid = 0;
		attribute = 0;
		value = 0;
	}

	SetServerAttrArg(int _gmuserid_, int _localsid_, int _attribute_, int _value_)
		: gmuserid(_gmuserid_), localsid(_localsid_), attribute(_attribute_), value(_value_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << gmuserid;
		_os_ << localsid;
		_os_ << attribute;
		_os_ << value;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> gmuserid;
		_os_ >> localsid;
		_os_ >> attribute;
		_os_ >> value;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SetServerAttrArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SetServerAttrArg *o = dynamic_cast<const SetServerAttrArg*>(&d)) return *this = *o;
		return *this;
	}

	SetServerAttrArg& operator=(const SetServerAttrArg & d)
	{
		if (&d == this) return *this;
		gmuserid = d.gmuserid;
		localsid = d.localsid;
		attribute = d.attribute;
		value = d.value;
		return *this;
	}

	bool operator<(const SetServerAttrArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SetServerAttrArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = gmuserid - _o_.gmuserid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = attribute - _o_.attribute;
		if (0 != _c_) return _c_;
		_c_ = value - _o_.value;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
