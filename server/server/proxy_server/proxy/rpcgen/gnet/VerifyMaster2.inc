	// Protocol

	enum { PROTOCOL_TYPE = 604 };

	int zoneid;
	int64_t roleid;
	GNET::Octets rolename;
	GNET::Octets faction;

	VerifyMaster2() {
		type = PROTOCOL_TYPE;
		zoneid = 0;
		roleid = -1;
	}

	VerifyMaster2(int _zoneid_, int64_t _roleid_, const GNET::Octets & _rolename_, const GNET::Octets & _faction_)
		: zoneid(_zoneid_), roleid(_roleid_), rolename(_rolename_), faction(_faction_) {
		type = PROTOCOL_TYPE;
	}

	explicit VerifyMaster2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << roleid;
		_os_ << rolename;
		_os_ << faction;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> roleid;
		_os_ >> rolename;
		_os_ >> faction;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new VerifyMaster2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
