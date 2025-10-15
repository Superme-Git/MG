	// Protocol

	enum { PROTOCOL_TYPE = 10 };

	int userid;
	int localsid;
	char cause;

	KickoutUser() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		cause = GNET::ERR_KICKOUT;
	}

	KickoutUser(int _userid_, int _localsid_, char _cause_)
		: userid(_userid_), localsid(_localsid_), cause(_cause_) {
		type = PROTOCOL_TYPE;
	}

	explicit KickoutUser(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << cause;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> cause;
		return _os_;
	}

	bool operator<(const KickoutUser &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const KickoutUser &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = cause - _o_.cause;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new KickoutUser(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
