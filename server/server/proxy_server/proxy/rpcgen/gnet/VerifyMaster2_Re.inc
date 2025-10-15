	// Protocol

	enum { PROTOCOL_TYPE = 605 };

	GNET::Octets rolename;
	int ret;

	VerifyMaster2_Re() {
		type = PROTOCOL_TYPE;
		ret = -1;
	}

	VerifyMaster2_Re(const GNET::Octets & _rolename_, int _ret_)
		: rolename(_rolename_), ret(_ret_) {
		type = PROTOCOL_TYPE;
	}

	explicit VerifyMaster2_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << rolename;
		_os_ << ret;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> rolename;
		_os_ >> ret;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new VerifyMaster2_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
