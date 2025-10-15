	// Protocol

	enum { PROTOCOL_TYPE = 65544 };

	enum {
		E_START_LISTEN = 0,
		E_STOP_LISTEN = 1,
	};

	int ptype;

	LinkServerControl() {
		type = PROTOCOL_TYPE;
		ptype = 0;
	}

	explicit LinkServerControl(int _ptype_)
		: ptype(_ptype_) {
		type = PROTOCOL_TYPE;
	}

	explicit LinkServerControl(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << ptype;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> ptype;
		return _os_;
	}

	bool operator<(const LinkServerControl &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const LinkServerControl &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ptype - _o_.ptype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new LinkServerControl(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
