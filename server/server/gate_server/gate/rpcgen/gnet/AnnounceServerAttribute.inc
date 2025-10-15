	// Protocol

	enum { PROTOCOL_TYPE = 125 };

	gnet::ServerAttr serverattr;

	AnnounceServerAttribute() {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceServerAttribute(const gnet::ServerAttr & _serverattr_)
		: serverattr(_serverattr_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceServerAttribute(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!serverattr._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << serverattr;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> serverattr;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new AnnounceServerAttribute(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
