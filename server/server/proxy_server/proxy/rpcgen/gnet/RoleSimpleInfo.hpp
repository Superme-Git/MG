
#pragma once


namespace gnet { 

class RoleSimpleInfo : public GNET::Rpc::Data
{
public:
	int64_t roleid;
	GNET::Octets rolename;
	int level;

	RoleSimpleInfo() {
		roleid = 0;
		level = 0;
	}

	RoleSimpleInfo(int64_t _roleid_, const GNET::Octets & _rolename_, int _level_)
		: roleid(_roleid_), rolename(_rolename_), level(_level_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << rolename;
		_os_ << level;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> rolename;
		_os_ >> level;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new RoleSimpleInfo(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const RoleSimpleInfo *o = dynamic_cast<const RoleSimpleInfo*>(&d)) return *this = *o;
		return *this;
	}

	RoleSimpleInfo& operator=(const RoleSimpleInfo & d)
	{
		if (&d == this) return *this;
		roleid = d.roleid;
		rolename = d.rolename;
		level = d.level;
		return *this;
	}

};

} 
