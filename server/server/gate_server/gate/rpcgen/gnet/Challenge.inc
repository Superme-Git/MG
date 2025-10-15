	// Protocol

	enum { PROTOCOL_TYPE = 101 };

	GNET::Octets nonce;
	int version;
	gnet::ServerAttr serverattr;

	Challenge() {
		type = PROTOCOL_TYPE;
		version = 0;
	}

	Challenge(const GNET::Octets & _nonce_, int _version_, const gnet::ServerAttr & _serverattr_)
		: nonce(_nonce_), version(_version_), serverattr(_serverattr_) {
		type = PROTOCOL_TYPE;
	}

	explicit Challenge(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!serverattr._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << nonce;
		_os_ << version;
		_os_ << serverattr;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> nonce;
		_os_ >> version;
		_os_ >> serverattr;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Challenge(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
