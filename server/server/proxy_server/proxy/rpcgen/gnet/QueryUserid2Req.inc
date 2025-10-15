	// Protocol

	enum { PROTOCOL_TYPE = 212 };

	int xid;
	GNET::Octets rolename;

	QueryUserid2Req() {
		type = PROTOCOL_TYPE;
		xid = 0;
	}

	QueryUserid2Req(int _xid_, const GNET::Octets & _rolename_)
		: xid(_xid_), rolename(_rolename_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryUserid2Req(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << xid;
		_os_ << rolename;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> xid;
		_os_ >> rolename;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new QueryUserid2Req(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
