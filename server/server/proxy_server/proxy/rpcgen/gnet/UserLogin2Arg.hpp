
#pragma once


namespace gnet { 

class UserLogin2Arg : public GNET::Rpc::Data
{
public:
	int userid;
	int localsid;
	char blkickuser;
	int freecreatime;
	GNET::Octets used_elec_number;
	int reserved1;
	GNET::Octets reserved2;

	UserLogin2Arg() {
		userid = -1;
		localsid = 0;
		blkickuser = 0;
		freecreatime = 0;
		reserved1 = 0;
	}

	UserLogin2Arg(int _userid_, int _localsid_, char _blkickuser_, int _freecreatime_, const GNET::Octets & _used_elec_number_, int _reserved1_, const GNET::Octets & _reserved2_)
		: userid(_userid_), localsid(_localsid_), blkickuser(_blkickuser_), freecreatime(_freecreatime_), used_elec_number(_used_elec_number_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << blkickuser;
		_os_ << freecreatime;
		_os_ << used_elec_number;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> blkickuser;
		_os_ >> freecreatime;
		_os_ >> used_elec_number;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLogin2Arg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLogin2Arg *o = dynamic_cast<const UserLogin2Arg*>(&d)) return *this = *o;
		return *this;
	}

	UserLogin2Arg& operator=(const UserLogin2Arg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		localsid = d.localsid;
		blkickuser = d.blkickuser;
		freecreatime = d.freecreatime;
		used_elec_number = d.used_elec_number;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
