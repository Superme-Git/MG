	// Protocol

	enum { PROTOCOL_TYPE = 786437 };

	int64_t roleid;
	int rolesnum; // 同屏能看见的人数

	CEnterWorld() {
		type = PROTOCOL_TYPE;
		roleid = 0;
		rolesnum = 0;
	}

	CEnterWorld(int64_t _roleid_, int _rolesnum_)
		: roleid(_roleid_), rolesnum(_rolesnum_) {
		type = PROTOCOL_TYPE;
	}

	explicit CEnterWorld(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (roleid < 1) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << roleid;
		_os_ << rolesnum;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> rolesnum;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CEnterWorld &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CEnterWorld &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = FireNet::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = rolesnum - _o_.rolesnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CEnterWorld(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
