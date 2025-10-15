	// Protocol

	enum { PROTOCOL_TYPE = 857022 };

	int captype;
	GNET::Octets captcha;

	ACCaptcha() {
		type = PROTOCOL_TYPE;
		captype = 0;
	}

	ACCaptcha(int _captype_, const GNET::Octets & _captcha_)
		: captype(_captype_), captcha(_captcha_) {
		type = PROTOCOL_TYPE;
	}

	explicit ACCaptcha(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
#ifdef VALIDATE_PROTOCOL
#endif
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << captype;
		_os_ << captcha;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> captype;
		_os_ >> captcha;
		return _os_;
	}

	aio::Protocol * Clone() const { return new ACCaptcha(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 16384; }
