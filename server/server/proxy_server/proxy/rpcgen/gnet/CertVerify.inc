	// Protocol

	enum { PROTOCOL_TYPE = 124 };

	int localsid;
	GNET::Octets authd_cert;

	CertVerify() {
		type = PROTOCOL_TYPE;
		localsid = 0;
	}

	CertVerify(int _localsid_, const GNET::Octets & _authd_cert_)
		: localsid(_localsid_), authd_cert(_authd_cert_) {
		type = PROTOCOL_TYPE;
	}

	explicit CertVerify(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << localsid;
		_os_ << authd_cert;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> localsid;
		_os_ >> authd_cert;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new CertVerify(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
