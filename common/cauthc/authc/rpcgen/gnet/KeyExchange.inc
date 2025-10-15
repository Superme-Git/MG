	// Protocol

	enum { PROTOCOL_TYPE = 106 };

	FireNet::Octets nonce;
	int blkickuser;

	KeyExchange() {
		type = PROTOCOL_TYPE;
		blkickuser = 0;
	}

	KeyExchange(const FireNet::Octets & _nonce_, int _blkickuser_)
		: nonce(_nonce_), blkickuser(_blkickuser_) {
		type = PROTOCOL_TYPE;
	}

	explicit KeyExchange(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << nonce;
		_os_ << blkickuser;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> nonce;
		_os_ >> blkickuser;
		return _os_;
	}

	aio::Protocol * Clone() const { return new KeyExchange(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
