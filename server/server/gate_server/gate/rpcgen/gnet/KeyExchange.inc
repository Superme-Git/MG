	// Protocol

	enum { PROTOCOL_TYPE = 106 };

	GNET::Octets nonce;
	int blkickuser;

	KeyExchange() {
		type = PROTOCOL_TYPE;
		blkickuser = 0;
	}

	KeyExchange(const GNET::Octets & _nonce_, int _blkickuser_)
		: nonce(_nonce_), blkickuser(_blkickuser_) {
		type = PROTOCOL_TYPE;
	}

	explicit KeyExchange(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << nonce;
		_os_ << blkickuser;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> nonce;
		_os_ >> blkickuser;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new KeyExchange(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
