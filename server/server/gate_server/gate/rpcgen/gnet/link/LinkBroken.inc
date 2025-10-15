	// Protocol

	enum { PROTOCOL_TYPE = 65546 };

	enum {
		REASON_PEERCLOSE = 0,
	};

	int userid;
	int linksid;
	int reason;

	LinkBroken() {
		type = PROTOCOL_TYPE;
		userid = 0;
		linksid = 0;
		reason = 0;
	}

	LinkBroken(int _userid_, int _linksid_, int _reason_)
		: userid(_userid_), linksid(_linksid_), reason(_reason_) {
		type = PROTOCOL_TYPE;
	}

	explicit LinkBroken(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << linksid;
		_os_ << reason;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> linksid;
		_os_ >> reason;
		return _os_;
	}

	bool operator<(const LinkBroken &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const LinkBroken &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = linksid - _o_.linksid;
		if (0 != _c_) return _c_;
		_c_ = reason - _o_.reason;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new LinkBroken(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
