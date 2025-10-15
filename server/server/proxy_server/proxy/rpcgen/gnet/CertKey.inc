	// Protocol

	enum { PROTOCOL_TYPE = 547 };

	GNET::Octets d_key1_encrypt;
	GNET::Octets d_key2_encrypt;
	int reserved1;

	CertKey() {
		type = PROTOCOL_TYPE;
		reserved1 = 0;
	}

	CertKey(const GNET::Octets & _d_key1_encrypt_, const GNET::Octets & _d_key2_encrypt_, int _reserved1_)
		: d_key1_encrypt(_d_key1_encrypt_), d_key2_encrypt(_d_key2_encrypt_), reserved1(_reserved1_) {
		type = PROTOCOL_TYPE;
	}

	explicit CertKey(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << d_key1_encrypt;
		_os_ << d_key2_encrypt;
		_os_ << reserved1;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> d_key1_encrypt;
		_os_ >> d_key2_encrypt;
		_os_ >> reserved1;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new CertKey(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
