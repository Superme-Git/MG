	// Protocol

	enum { PROTOCOL_TYPE = 357 };

	int retcode;
	int dstuserid;
	int forbid_time;

	GMShutup_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		dstuserid = 0;
		forbid_time = 0;
	}

	GMShutup_Re(int _retcode_, int _dstuserid_, int _forbid_time_)
		: retcode(_retcode_), dstuserid(_dstuserid_), forbid_time(_forbid_time_) {
		type = PROTOCOL_TYPE;
	}

	explicit GMShutup_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << dstuserid;
		_os_ << forbid_time;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> dstuserid;
		_os_ >> forbid_time;
		return _os_;
	}

	bool operator<(const GMShutup_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GMShutup_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = dstuserid - _o_.dstuserid;
		if (0 != _c_) return _c_;
		_c_ = forbid_time - _o_.forbid_time;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new GMShutup_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
