	// Protocol

	enum { PROTOCOL_TYPE = 551 };

	int algorithm;
	int nonce;

	MatrixChallenge() {
		type = PROTOCOL_TYPE;
		algorithm = 0;
		nonce = 0;
	}

	MatrixChallenge(int _algorithm_, int _nonce_)
		: algorithm(_algorithm_), nonce(_nonce_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixChallenge(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << algorithm;
		_os_ << nonce;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> algorithm;
		_os_ >> nonce;
		return _os_;
	}

	bool operator<(const MatrixChallenge &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MatrixChallenge &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = algorithm - _o_.algorithm;
		if (0 != _c_) return _c_;
		_c_ = nonce - _o_.nonce;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new MatrixChallenge(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
