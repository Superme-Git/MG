	// Protocol

	enum { PROTOCOL_TYPE = 126 };

	int forbid_ip;

	DeliverAnnounceToLink() {
		type = PROTOCOL_TYPE;
		forbid_ip = 0;
	}

	explicit DeliverAnnounceToLink(int _forbid_ip_)
		: forbid_ip(_forbid_ip_) {
		type = PROTOCOL_TYPE;
	}

	explicit DeliverAnnounceToLink(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << forbid_ip;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> forbid_ip;
		return _os_;
	}

	bool operator<(const DeliverAnnounceToLink &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const DeliverAnnounceToLink &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = forbid_ip - _o_.forbid_ip;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new DeliverAnnounceToLink(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
