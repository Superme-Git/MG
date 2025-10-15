	// Protocol

	enum { PROTOCOL_TYPE = 225 };

	enum {
		RET_OK = 0,
		RET_CONFLICT_ZONE = 1,
		RET_INVALID_ZONE = 2,
	};

	int retcode;
	int cross_zoneid;
	int64_t boottime;

	AnnounceZoneidToCross_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		cross_zoneid = 0;
		boottime = 0;
	}

	AnnounceZoneidToCross_Re(int _retcode_, int _cross_zoneid_, int64_t _boottime_)
		: retcode(_retcode_), cross_zoneid(_cross_zoneid_), boottime(_boottime_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceZoneidToCross_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << cross_zoneid;
		_os_ << boottime;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> cross_zoneid;
		_os_ >> boottime;
		return _os_;
	}

	bool operator<(const AnnounceZoneidToCross_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AnnounceZoneidToCross_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = cross_zoneid - _o_.cross_zoneid;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(boottime - _o_.boottime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AnnounceZoneidToCross_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
