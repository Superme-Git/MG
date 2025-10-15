	// Protocol

	enum { PROTOCOL_TYPE = 65547 };

	int linkid;

	AnnounceLinkId() {
		type = PROTOCOL_TYPE;
		linkid = 0;
	}

	explicit AnnounceLinkId(int _linkid_)
		: linkid(_linkid_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceLinkId(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << linkid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> linkid;
		return _os_;
	}

	bool operator<(const AnnounceLinkId &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AnnounceLinkId &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = linkid - _o_.linkid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AnnounceLinkId(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
