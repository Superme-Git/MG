	// Protocol

	enum { PROTOCOL_TYPE = 202 };

	int xid;
	int userid;
	int zoneid;
	char force;

	GetAddCashSN2Req() {
		type = PROTOCOL_TYPE;
		xid = -1;
		userid = -1;
		zoneid = -1;
		force = -1;
	}

	GetAddCashSN2Req(int _xid_, int _userid_, int _zoneid_, char _force_)
		: xid(_xid_), userid(_userid_), zoneid(_zoneid_), force(_force_) {
		type = PROTOCOL_TYPE;
	}

	explicit GetAddCashSN2Req(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << xid;
		_os_ << userid;
		_os_ << zoneid;
		_os_ << force;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> xid;
		_os_ >> userid;
		_os_ >> zoneid;
		_os_ >> force;
		return _os_;
	}

	bool operator<(const GetAddCashSN2Req &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetAddCashSN2Req &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = force - _o_.force;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new GetAddCashSN2Req(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
