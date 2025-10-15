	// Protocol

	enum { PROTOCOL_TYPE = 65539 };

	enum {
		E_PROTOCOL_UNKOWN = 1,
		E_MARSHAL_EXCEPTION = 2,
		E_PROTOCOL_EXCEPTION = 3,
		A_QUICK_CLOSE = 1,
		A_DELAY_CLOSE = 2,
		A_ACKICKOUT = 3,
	};

	int linksid;
	int action;
	int error;

	Kick() {
		type = PROTOCOL_TYPE;
		linksid = 0;
		action = 0;
		error = 0;
	}

	Kick(int _linksid_, int _action_, int _error_)
		: linksid(_linksid_), action(_action_), error(_error_) {
		type = PROTOCOL_TYPE;
	}

	explicit Kick(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << linksid;
		_os_ << action;
		_os_ << error;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> linksid;
		_os_ >> action;
		_os_ >> error;
		return _os_;
	}

	bool operator<(const Kick &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const Kick &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = linksid - _o_.linksid;
		if (0 != _c_) return _c_;
		_c_ = action - _o_.action;
		if (0 != _c_) return _c_;
		_c_ = error - _o_.error;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new Kick(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
