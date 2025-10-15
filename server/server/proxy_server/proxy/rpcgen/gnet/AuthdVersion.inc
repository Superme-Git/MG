	// Protocol

	enum { PROTOCOL_TYPE = 8010 };

	enum {
		RET_OK = 0,
		RET_ZONEID_INVALID = 1,
		RET_ZONEID_CONFLICT = 2,
	};

	int version;
	int retcode;
	int reserved;

	AuthdVersion() {
		type = PROTOCOL_TYPE;
		version = 0;
		retcode = 0;
		reserved = 0;
	}

	AuthdVersion(int _version_, int _retcode_, int _reserved_)
		: version(_version_), retcode(_retcode_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit AuthdVersion(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << version;
		_os_ << retcode;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> version;
		_os_ >> retcode;
		_os_ >> reserved;
		return _os_;
	}

	bool operator<(const AuthdVersion &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AuthdVersion &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = version - _o_.version;
		if (0 != _c_) return _c_;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AuthdVersion(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
