	// Protocol

	enum { PROTOCOL_TYPE = 100 };

	int code;

	KeepAlive() {
		type = PROTOCOL_TYPE;
		code = 0;
	}

	explicit KeepAlive(int _code_)
		: code(_code_) {
		type = PROTOCOL_TYPE;
	}

	explicit KeepAlive(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << code;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> code;
		return _os_;
	}

	bool operator<(const KeepAlive &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const KeepAlive &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = code - _o_.code;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new KeepAlive(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 16; }
