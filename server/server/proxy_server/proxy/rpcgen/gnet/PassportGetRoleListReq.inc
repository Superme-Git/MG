	// Protocol

	enum { PROTOCOL_TYPE = 214 };

	int xid;
	int userid;

	PassportGetRoleListReq() {
		type = PROTOCOL_TYPE;
		xid = 0;
		userid = 0;
	}

	PassportGetRoleListReq(int _xid_, int _userid_)
		: xid(_xid_), userid(_userid_) {
		type = PROTOCOL_TYPE;
	}

	explicit PassportGetRoleListReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << xid;
		_os_ << userid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> xid;
		_os_ >> userid;
		return _os_;
	}

	bool operator<(const PassportGetRoleListReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PassportGetRoleListReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new PassportGetRoleListReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
