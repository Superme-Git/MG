
#pragma once


namespace gnet { 

class UserLogoutRes : public GNET::Rpc::Data
{
public:
	char retcode;

	UserLogoutRes() {
		retcode = -1;
	}

	UserLogoutRes(char _retcode_)
		: retcode(_retcode_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLogoutRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLogoutRes *o = dynamic_cast<const UserLogoutRes*>(&d)) return *this = *o;
		return *this;
	}

	UserLogoutRes& operator=(const UserLogoutRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		return *this;
	}

	bool operator<(const UserLogoutRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLogoutRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
