	// Protocol

	enum { PROTOCOL_TYPE = 215 };

	int xid;
	int retcode; // 0成功，1没有角色,2网络通信错误
	std::vector<gnet::RoleSimpleInfo> rolelist; // 角色列表

	PassportGetRoleListRep() {
		type = PROTOCOL_TYPE;
		xid = 0;
		retcode = 0;
	}

	PassportGetRoleListRep(int _xid_, int _retcode_, const std::vector<gnet::RoleSimpleInfo> & _rolelist_)
		: xid(_xid_), retcode(_retcode_), rolelist(_rolelist_) {
		type = PROTOCOL_TYPE;
	}

	explicit PassportGetRoleListRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<gnet::RoleSimpleInfo>::const_iterator _i_ = rolelist.begin(); _i_ != rolelist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << xid;
		_os_ << retcode;
		_os_ << GNET::MarshalContainer(rolelist);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> xid;
		_os_ >> retcode;
		_os_ >> GNET::MarshalContainer(rolelist);
		return _os_;
	}

	GNET::Protocol * Clone() const { return new PassportGetRoleListRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
