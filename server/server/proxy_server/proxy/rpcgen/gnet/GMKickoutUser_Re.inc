	// Protocol

	enum { PROTOCOL_TYPE = 355 };

	int retcode;
	int gmuserid;
	int localsid;
	int kickuserid;

	GMKickoutUser_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		gmuserid = 0;
		localsid = 0;
		kickuserid = 0;
	}

	GMKickoutUser_Re(int _retcode_, int _gmuserid_, int _localsid_, int _kickuserid_)
		: retcode(_retcode_), gmuserid(_gmuserid_), localsid(_localsid_), kickuserid(_kickuserid_) {
		type = PROTOCOL_TYPE;
	}

	explicit GMKickoutUser_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << gmuserid;
		_os_ << localsid;
		_os_ << kickuserid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> gmuserid;
		_os_ >> localsid;
		_os_ >> kickuserid;
		return _os_;
	}

	bool operator<(const GMKickoutUser_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GMKickoutUser_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = gmuserid - _o_.gmuserid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = kickuserid - _o_.kickuserid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new GMKickoutUser_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
