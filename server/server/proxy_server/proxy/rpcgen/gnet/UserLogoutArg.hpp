
#pragma once


namespace gnet { 

class UserLogoutArg : public GNET::Rpc::Data
{
public:
	int userid;
	int localsid;

	UserLogoutArg() {
		userid = -1;
		localsid = 0;
	}

	UserLogoutArg(int _userid_, int _localsid_)
		: userid(_userid_), localsid(_localsid_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLogoutArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLogoutArg *o = dynamic_cast<const UserLogoutArg*>(&d)) return *this = *o;
		return *this;
	}

	UserLogoutArg& operator=(const UserLogoutArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		localsid = d.localsid;
		return *this;
	}

	bool operator<(const UserLogoutArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLogoutArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
