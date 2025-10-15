
#pragma once

#include "gnet/RoleSimpleInfo.hpp"
#include <vector>

namespace gnet { 

class PassportGetRoleListRes : public GNET::Rpc::Data
{
public:
	int retcode;
	std::vector<gnet::RoleSimpleInfo> rolelist; // ╫ги╚ап╠М

	PassportGetRoleListRes() {
		retcode = 0;
	}

	PassportGetRoleListRes(int _retcode_, const std::vector<gnet::RoleSimpleInfo> & _rolelist_)
		: retcode(_retcode_), rolelist(_rolelist_) {
	}

	bool _validator_() const {
		for (std::vector<gnet::RoleSimpleInfo>::const_iterator _i_ = rolelist.begin(); _i_ != rolelist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << GNET::MarshalContainer(rolelist);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> GNET::MarshalContainer(rolelist);
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new PassportGetRoleListRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const PassportGetRoleListRes *o = dynamic_cast<const PassportGetRoleListRes*>(&d)) return *this = *o;
		return *this;
	}

	PassportGetRoleListRes& operator=(const PassportGetRoleListRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		rolelist = d.rolelist;
		return *this;
	}

};

} 
