	// Protocol

	enum { PROTOCOL_TYPE = 793448 };

	int fighterindex;

	SRemoveWatcher() {
		type = PROTOCOL_TYPE;
		fighterindex = 0;
	}

	explicit SRemoveWatcher(int _fighterindex_)
		: fighterindex(_fighterindex_) {
		type = PROTOCOL_TYPE;
	}

	explicit SRemoveWatcher(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << fighterindex;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> fighterindex;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const SRemoveWatcher &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SRemoveWatcher &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterindex - _o_.fighterindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new SRemoveWatcher(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
