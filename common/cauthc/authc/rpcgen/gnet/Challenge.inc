	// Protocol

	enum { PROTOCOL_TYPE = 101 };

	FireNet::Octets nonce;
	int version;
	gnet::ServerAttr mserverattr;

	Challenge() {
		type = PROTOCOL_TYPE;
		version = 0;
	}

	Challenge(const FireNet::Octets & _nonce_, int _version_, const gnet::ServerAttr & _mserverattr_)
		: nonce(_nonce_), version(_version_), mserverattr(_mserverattr_) {
		type = PROTOCOL_TYPE;
	}

	explicit Challenge(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!mserverattr._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << nonce;
		_os_ << version;
		_os_ << mserverattr;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> nonce;
		_os_ >> version;
		_os_ >> mserverattr;
		return _os_;
	}

	aio::Protocol * Clone() const { return new Challenge(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
