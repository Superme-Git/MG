	// Protocol

	enum { PROTOCOL_TYPE = 209 };

	int retcode;
	int userid;
	int localsid;
	int reserved;

	InstantAddCashRep() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		userid = 0;
		localsid = 0;
		reserved = 0;
	}

	InstantAddCashRep(int _retcode_, int _userid_, int _localsid_, int _reserved_)
		: retcode(_retcode_), userid(_userid_), localsid(_localsid_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit InstantAddCashRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << localsid;
		_os_ << reserved;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> reserved;
		return _os_;
	}

	bool operator<(const InstantAddCashRep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const InstantAddCashRep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new InstantAddCashRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
