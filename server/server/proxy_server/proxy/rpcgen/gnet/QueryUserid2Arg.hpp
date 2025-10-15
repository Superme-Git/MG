
#pragma once


namespace gnet { 

class QueryUserid2Arg : public GNET::Rpc::Data
{
public:
	GNET::Octets rolename;

	QueryUserid2Arg() {
	}

	QueryUserid2Arg(const GNET::Octets & _rolename_)
		: rolename(_rolename_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << rolename;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> rolename;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new QueryUserid2Arg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const QueryUserid2Arg *o = dynamic_cast<const QueryUserid2Arg*>(&d)) return *this = *o;
		return *this;
	}

	QueryUserid2Arg& operator=(const QueryUserid2Arg & d)
	{
		if (&d == this) return *this;
		rolename = d.rolename;
		return *this;
	}

};

} 
