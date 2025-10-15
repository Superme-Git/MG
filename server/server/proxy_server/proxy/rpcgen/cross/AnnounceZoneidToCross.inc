	// Protocol

	enum { PROTOCOL_TYPE = 224 };

	int zoneid;
	int64_t boottime;

	AnnounceZoneidToCross() {
		type = PROTOCOL_TYPE;
		zoneid = 0;
		boottime = 0;
	}

	AnnounceZoneidToCross(int _zoneid_, int64_t _boottime_)
		: zoneid(_zoneid_), boottime(_boottime_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceZoneidToCross(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << boottime;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> boottime;
		return _os_;
	}

	bool operator<(const AnnounceZoneidToCross &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AnnounceZoneidToCross &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(boottime - _o_.boottime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AnnounceZoneidToCross(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
