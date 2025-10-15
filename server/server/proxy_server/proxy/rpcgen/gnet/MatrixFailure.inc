	// Protocol

	enum { PROTOCOL_TYPE = 553 };

	int userid;
	int loginip;
	int weight;

	MatrixFailure() {
		type = PROTOCOL_TYPE;
		userid = 0;
		loginip = 0;
		weight = 0;
	}

	MatrixFailure(int _userid_, int _loginip_, int _weight_)
		: userid(_userid_), loginip(_loginip_), weight(_weight_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixFailure(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << loginip;
		_os_ << weight;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> loginip;
		_os_ >> weight;
		return _os_;
	}

	bool operator<(const MatrixFailure &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MatrixFailure &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = loginip - _o_.loginip;
		if (0 != _c_) return _c_;
		_c_ = weight - _o_.weight;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new MatrixFailure(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
