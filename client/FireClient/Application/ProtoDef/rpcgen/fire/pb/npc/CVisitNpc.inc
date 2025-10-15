	// Protocol

	enum { PROTOCOL_TYPE = 795433 };

	int64_t npckey; // npckeyÎªnpcµÄÎ¨Ò»ID

	CVisitNpc() {
		type = PROTOCOL_TYPE;
		npckey = 0;
	}

	explicit CVisitNpc(int64_t _npckey_)
		: npckey(_npckey_) {
		type = PROTOCOL_TYPE;
	}

	explicit CVisitNpc(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (npckey < 0) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << npckey;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> npckey;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CVisitNpc &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CVisitNpc &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = FireNet::signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CVisitNpc(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
