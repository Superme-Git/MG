	// Protocol

	enum { PROTOCOL_TYPE = 790433 };

	fire::pb::move::Pos srcpos; // 出发点
	fire::pb::move::Pos destpos; // 到达点
	int64_t sceneid; // 场景id

	CRoleMove() {
		type = PROTOCOL_TYPE;
		sceneid = 0;
	}

	CRoleMove(const fire::pb::move::Pos & _srcpos_, const fire::pb::move::Pos & _destpos_, int64_t _sceneid_)
		: srcpos(_srcpos_), destpos(_destpos_), sceneid(_sceneid_) {
		type = PROTOCOL_TYPE;
	}

	explicit CRoleMove(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!srcpos._validator_()) return false;
		if (!destpos._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << srcpos;
		_os_ << destpos;
		_os_ << sceneid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> srcpos;
		_os_ >> destpos;
		_os_ >> sceneid;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CRoleMove(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
