
#pragma once


namespace gnet { 

class UserLoginArg : public GNET::Rpc::Data
{
public:
	int userid;
	int localsid;
	char blkickuser;
	int freecreatime;

	UserLoginArg() {
		userid = -1;
		localsid = 0;
		blkickuser = 0;
		freecreatime = 0;
	}

	UserLoginArg(int _userid_, int _localsid_, char _blkickuser_, int _freecreatime_)
		: userid(_userid_), localsid(_localsid_), blkickuser(_blkickuser_), freecreatime(_freecreatime_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << blkickuser;
		_os_ << freecreatime;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> blkickuser;
		_os_ >> freecreatime;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLoginArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLoginArg *o = dynamic_cast<const UserLoginArg*>(&d)) return *this = *o;
		return *this;
	}

	UserLoginArg& operator=(const UserLoginArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		localsid = d.localsid;
		blkickuser = d.blkickuser;
		freecreatime = d.freecreatime;
		return *this;
	}

	bool operator<(const UserLoginArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLoginArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = blkickuser - _o_.blkickuser;
		if (0 != _c_) return _c_;
		_c_ = freecreatime - _o_.freecreatime;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
