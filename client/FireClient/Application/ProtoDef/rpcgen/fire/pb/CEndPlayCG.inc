	// Protocol

	enum { PROTOCOL_TYPE = 786456 };

	int id; // ¶¯»­µÄid

	CEndPlayCG() {
		type = PROTOCOL_TYPE;
		id = 0;
	}

	explicit CEndPlayCG(int _id_)
		: id(_id_) {
		type = PROTOCOL_TYPE;
	}

	explicit CEndPlayCG(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (id <= 0) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << id;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CEndPlayCG &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CEndPlayCG &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CEndPlayCG(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
