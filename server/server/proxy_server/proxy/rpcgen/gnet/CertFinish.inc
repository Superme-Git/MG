	// Protocol

	enum { PROTOCOL_TYPE = 548 };

	int reserved1;

	CertFinish() {
		type = PROTOCOL_TYPE;
		reserved1 = 0;
	}

	explicit CertFinish(int _reserved1_)
		: reserved1(_reserved1_) {
		type = PROTOCOL_TYPE;
	}

	explicit CertFinish(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << reserved1;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> reserved1;
		return _os_;
	}

	bool operator<(const CertFinish &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CertFinish &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new CertFinish(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
