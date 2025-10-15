
#pragma once


namespace gnet { 

class GetAddCashSN2Res : public GNET::Rpc::Data
{
public:
	int retcode;
	int userid;
	int zoneid;
	int sn;

	GetAddCashSN2Res() {
		retcode = -1;
		userid = -1;
		zoneid = -1;
		sn = 0;
	}

	GetAddCashSN2Res(int _retcode_, int _userid_, int _zoneid_, int _sn_)
		: retcode(_retcode_), userid(_userid_), zoneid(_zoneid_), sn(_sn_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << zoneid;
		_os_ << sn;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> zoneid;
		_os_ >> sn;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetAddCashSN2Res(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetAddCashSN2Res *o = dynamic_cast<const GetAddCashSN2Res*>(&d)) return *this = *o;
		return *this;
	}

	GetAddCashSN2Res& operator=(const GetAddCashSN2Res & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		userid = d.userid;
		zoneid = d.zoneid;
		sn = d.sn;
		return *this;
	}

	bool operator<(const GetAddCashSN2Res &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetAddCashSN2Res &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = sn - _o_.sn;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
