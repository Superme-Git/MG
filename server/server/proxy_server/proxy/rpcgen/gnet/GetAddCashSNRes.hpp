
#pragma once


namespace gnet { 

class GetAddCashSNRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int userid;
	int zoneid;
	int sn;

	GetAddCashSNRes() {
		retcode = -1;
		userid = -1;
		zoneid = -1;
		sn = 0;
	}

	GetAddCashSNRes(int _retcode_, int _userid_, int _zoneid_, int _sn_)
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
		return new GetAddCashSNRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetAddCashSNRes *o = dynamic_cast<const GetAddCashSNRes*>(&d)) return *this = *o;
		return *this;
	}

	GetAddCashSNRes& operator=(const GetAddCashSNRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		userid = d.userid;
		zoneid = d.zoneid;
		sn = d.sn;
		return *this;
	}

	bool operator<(const GetAddCashSNRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetAddCashSNRes &_o_) const {
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
