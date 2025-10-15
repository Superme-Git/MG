	// Protocol

	enum { PROTOCOL_TYPE = 120 };

	int response;
	int localsid;

	MatrixResponseReq() {
		type = PROTOCOL_TYPE;
		response = 0;
		localsid = 0;
	}

	MatrixResponseReq(int _response_, int _localsid_)
		: response(_response_), localsid(_localsid_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixResponseReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << response;
		_os_ << localsid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> response;
		_os_ >> localsid;
		return _os_;
	}

	bool operator<(const MatrixResponseReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MatrixResponseReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = response - _o_.response;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new MatrixResponseReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
