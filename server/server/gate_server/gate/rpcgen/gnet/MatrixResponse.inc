	// Protocol

	enum { PROTOCOL_TYPE = 552 };

	int response;

	MatrixResponse() {
		type = PROTOCOL_TYPE;
		response = 0;
	}

	explicit MatrixResponse(int _response_)
		: response(_response_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixResponse(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << response;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> response;
		return _os_;
	}

	bool operator<(const MatrixResponse &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MatrixResponse &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = response - _o_.response;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new MatrixResponse(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
