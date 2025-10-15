	// Protocol

	enum { PROTOCOL_TYPE = 8901 };

	int plattype; // 当前服务器组，登录的是那个平台

	RegistGameServerResponse() {
		type = PROTOCOL_TYPE;
		plattype = 0;
	}

	explicit RegistGameServerResponse(int _plattype_)
		: plattype(_plattype_) {
		type = PROTOCOL_TYPE;
	}

	explicit RegistGameServerResponse(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << plattype;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> plattype;
		return _os_;
	}

	bool operator<(const RegistGameServerResponse &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RegistGameServerResponse &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = plattype - _o_.plattype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new RegistGameServerResponse(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
