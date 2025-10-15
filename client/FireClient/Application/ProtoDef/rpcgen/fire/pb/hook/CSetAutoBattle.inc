	// Protocol

	enum { PROTOCOL_TYPE = 810334 };

	char isautobattle; // 设置是否自动战斗

	CSetAutoBattle() {
		type = PROTOCOL_TYPE;
		isautobattle = 0;
	}

	explicit CSetAutoBattle(char _isautobattle_)
		: isautobattle(_isautobattle_) {
		type = PROTOCOL_TYPE;
	}

	explicit CSetAutoBattle(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << isautobattle;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> isautobattle;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CSetAutoBattle &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CSetAutoBattle &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = isautobattle - _o_.isautobattle;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CSetAutoBattle(*this); }
	int  PriorPolicy( ) const { return 101; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
