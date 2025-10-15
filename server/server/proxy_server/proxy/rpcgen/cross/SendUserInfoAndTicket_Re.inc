	// Protocol

	enum { PROTOCOL_TYPE = 229 };

	enum {
		RET_OK = 0,
		RET_NETWORK_ERROR = 1,
		RET_MULTI_LOGIN = 2,
		RET_SERVER_OVERLOAD = 3,
	};

	int retcode;
	int userid;
	char flag;

	SendUserInfoAndTicket_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		userid = 0;
		flag = 0;
	}

	SendUserInfoAndTicket_Re(int _retcode_, int _userid_, char _flag_)
		: retcode(_retcode_), userid(_userid_), flag(_flag_) {
		type = PROTOCOL_TYPE;
	}

	explicit SendUserInfoAndTicket_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << flag;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> flag;
		return _os_;
	}

	bool operator<(const SendUserInfoAndTicket_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SendUserInfoAndTicket_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new SendUserInfoAndTicket_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
