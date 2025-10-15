	// Protocol

	enum { PROTOCOL_TYPE = 213 };

	int xid;
	int result;
	int userid;
	int64_t roleid;
	int level;

	QueryUserid2Rep() {
		type = PROTOCOL_TYPE;
		xid = 0;
		result = 0;
		userid = 0;
		roleid = 0;
		level = 0;
	}

	QueryUserid2Rep(int _xid_, int _result_, int _userid_, int64_t _roleid_, int _level_)
		: xid(_xid_), result(_result_), userid(_userid_), roleid(_roleid_), level(_level_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryUserid2Rep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << xid;
		_os_ << result;
		_os_ << userid;
		_os_ << roleid;
		_os_ << level;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> xid;
		_os_ >> result;
		_os_ >> userid;
		_os_ >> roleid;
		_os_ >> level;
		return _os_;
	}

	bool operator<(const QueryUserid2Rep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const QueryUserid2Rep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new QueryUserid2Rep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
