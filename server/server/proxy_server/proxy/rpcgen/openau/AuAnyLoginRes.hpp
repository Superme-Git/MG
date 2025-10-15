
#pragma once

#include "openau/ErrorCodes.hpp"

namespace openau { 

class AuAnyLoginRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int userid;
	GNET::Octets platinfo;
	GNET::Octets realaccount;
	int reserved1;
	GNET::Octets reserved2;

	AuAnyLoginRes() {
		retcode = ErrorCodes::error_succeed;
		userid = -1;
		reserved1 = 0;
	}

	AuAnyLoginRes(int _retcode_, int _userid_, const GNET::Octets & _platinfo_, const GNET::Octets & _realaccount_, int _reserved1_, const GNET::Octets & _reserved2_)
		: retcode(_retcode_), userid(_userid_), platinfo(_platinfo_), realaccount(_realaccount_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << platinfo;
		_os_ << realaccount;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> platinfo;
		_os_ >> realaccount;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new AuAnyLoginRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const AuAnyLoginRes *o = dynamic_cast<const AuAnyLoginRes*>(&d)) return *this = *o;
		return *this;
	}

	AuAnyLoginRes& operator=(const AuAnyLoginRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		userid = d.userid;
		platinfo = d.platinfo;
		realaccount = d.realaccount;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
