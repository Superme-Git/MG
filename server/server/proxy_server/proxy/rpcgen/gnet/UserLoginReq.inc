	// Protocol

	enum { PROTOCOL_TYPE = 115 };

	gnet::UserLoginArg argument;

	UserLoginReq() {
		type = PROTOCOL_TYPE;
	}

	explicit UserLoginReq(const gnet::UserLoginArg & _argument_)
		: argument(_argument_) {
		type = PROTOCOL_TYPE;
	}

	explicit UserLoginReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!argument._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << argument;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> argument;
		return _os_;
	}

	bool operator<(const UserLoginReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLoginReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = argument.compareTo(_o_.argument);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new UserLoginReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
