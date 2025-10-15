	// Protocol

	enum { PROTOCOL_TYPE = 790447 };

	int64_t sceneid;
	fire::pb::move::Pos curpos;
	char locz; // RelocateRolePosµÄz×ø±ê

	CRelocateRolePos() {
		type = PROTOCOL_TYPE;
		sceneid = 0;
		locz = 0;
	}

	CRelocateRolePos(int64_t _sceneid_, const fire::pb::move::Pos & _curpos_, char _locz_)
		: sceneid(_sceneid_), curpos(_curpos_), locz(_locz_) {
		type = PROTOCOL_TYPE;
	}

	explicit CRelocateRolePos(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!curpos._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << sceneid;
		_os_ << curpos;
		_os_ << locz;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> sceneid;
		_os_ >> curpos;
		_os_ >> locz;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CRelocateRolePos(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
