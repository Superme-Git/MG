	// Protocol

	enum { PROTOCOL_TYPE = 117 };

	int userid;
	int localsid;
	int status;

	StatusAnnounce() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		status = 0;
	}

	StatusAnnounce(int _userid_, int _localsid_, int _status_)
		: userid(_userid_), localsid(_localsid_), status(_status_) {
		type = PROTOCOL_TYPE;
	}

	explicit StatusAnnounce(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << status;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> status;
		return _os_;
	}

	bool operator<(const StatusAnnounce &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const StatusAnnounce &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new StatusAnnounce(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
