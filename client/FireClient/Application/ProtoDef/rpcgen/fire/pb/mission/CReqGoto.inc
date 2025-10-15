	// Protocol

	enum { PROTOCOL_TYPE = 805516 };

	int mapid;
	int xpos;
	int ypos;

	CReqGoto() {
		type = PROTOCOL_TYPE;
		mapid = 0;
		xpos = 0;
		ypos = 0;
	}

	CReqGoto(int _mapid_, int _xpos_, int _ypos_)
		: mapid(_mapid_), xpos(_xpos_), ypos(_ypos_) {
		type = PROTOCOL_TYPE;
	}

	explicit CReqGoto(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << mapid;
		_os_ << xpos;
		_os_ << ypos;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> mapid;
		_os_ >> xpos;
		_os_ >> ypos;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CReqGoto &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CReqGoto &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = xpos - _o_.xpos;
		if (0 != _c_) return _c_;
		_c_ = ypos - _o_.ypos;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CReqGoto(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
