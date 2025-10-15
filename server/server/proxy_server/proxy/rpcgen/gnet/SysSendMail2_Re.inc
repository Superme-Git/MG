	// Protocol

	enum { PROTOCOL_TYPE = 4217 };

	int retcode;
	int tid;

	SysSendMail2_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		tid = 0;
	}

	SysSendMail2_Re(int _retcode_, int _tid_)
		: retcode(_retcode_), tid(_tid_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail2_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << tid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> tid;
		return _os_;
	}

	bool operator<(const SysSendMail2_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SysSendMail2_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = tid - _o_.tid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail2_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
