	// Protocol

	enum { PROTOCOL_TYPE = 527 };

	int zoneid;
	int aid;
	char blreset;
	int ip1;
	int ip2;
	int ip3;
	int version; // default 1
	int reserved;

	AnnounceZoneid3() {
		type = PROTOCOL_TYPE;
		zoneid = 0;
		aid = 0;
		blreset = 0;
		ip1 = 0;
		ip2 = 0;
		ip3 = 0;
		version = 0;
		reserved = 0;
	}

	AnnounceZoneid3(int _zoneid_, int _aid_, char _blreset_, int _ip1_, int _ip2_, int _ip3_, int _version_, int _reserved_)
		: zoneid(_zoneid_), aid(_aid_), blreset(_blreset_), ip1(_ip1_), ip2(_ip2_), ip3(_ip3_), version(_version_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceZoneid3(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << aid;
		_os_ << blreset;
		_os_ << ip1;
		_os_ << ip2;
		_os_ << ip3;
		_os_ << version;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> aid;
		_os_ >> blreset;
		_os_ >> ip1;
		_os_ >> ip2;
		_os_ >> ip3;
		_os_ >> version;
		_os_ >> reserved;
		return _os_;
	}

	bool operator<(const AnnounceZoneid3 &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AnnounceZoneid3 &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = aid - _o_.aid;
		if (0 != _c_) return _c_;
		_c_ = blreset - _o_.blreset;
		if (0 != _c_) return _c_;
		_c_ = ip1 - _o_.ip1;
		if (0 != _c_) return _c_;
		_c_ = ip2 - _o_.ip2;
		if (0 != _c_) return _c_;
		_c_ = ip3 - _o_.ip3;
		if (0 != _c_) return _c_;
		_c_ = version - _o_.version;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AnnounceZoneid3(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
