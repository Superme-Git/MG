	// Protocol

	enum { PROTOCOL_TYPE = 523 };

	int zoneid;
	int aid;
	char blreset;

	AnnounceZoneid2() {
		type = PROTOCOL_TYPE;
		zoneid = 0;
		aid = 0;
		blreset = 0;
	}

	AnnounceZoneid2(int _zoneid_, int _aid_, char _blreset_)
		: zoneid(_zoneid_), aid(_aid_), blreset(_blreset_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceZoneid2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << aid;
		_os_ << blreset;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> aid;
		_os_ >> blreset;
		return _os_;
	}

	bool operator<(const AnnounceZoneid2 &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AnnounceZoneid2 &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = aid - _o_.aid;
		if (0 != _c_) return _c_;
		_c_ = blreset - _o_.blreset;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AnnounceZoneid2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
