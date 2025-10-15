	// Protocol

	enum { PROTOCOL_TYPE = 546 };

	GNET::Octets authd_cert;
	int reserved1;

	CertAnswer() {
		type = PROTOCOL_TYPE;
		reserved1 = 0;
	}

	CertAnswer(const GNET::Octets & _authd_cert_, int _reserved1_)
		: authd_cert(_authd_cert_), reserved1(_reserved1_) {
		type = PROTOCOL_TYPE;
	}

	explicit CertAnswer(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << authd_cert;
		_os_ << reserved1;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> authd_cert;
		_os_ >> reserved1;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new CertAnswer(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
