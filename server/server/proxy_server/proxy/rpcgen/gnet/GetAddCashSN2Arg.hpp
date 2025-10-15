
#pragma once


namespace gnet { 

class GetAddCashSN2Arg : public GNET::Rpc::Data
{
public:
	int userid;
	int zoneid;
	char force;

	GetAddCashSN2Arg() {
		userid = -1;
		zoneid = -1;
		force = -1;
	}

	GetAddCashSN2Arg(int _userid_, int _zoneid_, char _force_)
		: userid(_userid_), zoneid(_zoneid_), force(_force_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << zoneid;
		_os_ << force;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> zoneid;
		_os_ >> force;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetAddCashSN2Arg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetAddCashSN2Arg *o = dynamic_cast<const GetAddCashSN2Arg*>(&d)) return *this = *o;
		return *this;
	}

	GetAddCashSN2Arg& operator=(const GetAddCashSN2Arg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		zoneid = d.zoneid;
		force = d.force;
		return *this;
	}

	bool operator<(const GetAddCashSN2Arg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetAddCashSN2Arg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = force - _o_.force;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
