	// Protocol

	enum { PROTOCOL_TYPE = 227 };

	cross::ServerInfo serverinfo;

	BroadcastCrossStatus_Re() {
		type = PROTOCOL_TYPE;
	}

	explicit BroadcastCrossStatus_Re(const cross::ServerInfo & _serverinfo_)
		: serverinfo(_serverinfo_) {
		type = PROTOCOL_TYPE;
	}

	explicit BroadcastCrossStatus_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!serverinfo._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << serverinfo;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> serverinfo;
		return _os_;
	}

	bool operator<(const BroadcastCrossStatus_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BroadcastCrossStatus_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = serverinfo.compareTo(_o_.serverinfo);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new BroadcastCrossStatus_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
