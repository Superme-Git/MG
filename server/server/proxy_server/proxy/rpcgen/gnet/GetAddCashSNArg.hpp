
#pragma once


namespace gnet { 

class GetAddCashSNArg : public GNET::Rpc::Data
{
public:
	int userid;
	int zoneid;

	GetAddCashSNArg() {
		userid = -1;
		zoneid = -1;
	}

	GetAddCashSNArg(int _userid_, int _zoneid_)
		: userid(_userid_), zoneid(_zoneid_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << zoneid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> zoneid;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetAddCashSNArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetAddCashSNArg *o = dynamic_cast<const GetAddCashSNArg*>(&d)) return *this = *o;
		return *this;
	}

	GetAddCashSNArg& operator=(const GetAddCashSNArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		zoneid = d.zoneid;
		return *this;
	}

	bool operator<(const GetAddCashSNArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetAddCashSNArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
