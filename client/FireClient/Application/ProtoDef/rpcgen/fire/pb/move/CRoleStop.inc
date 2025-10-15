	// Protocol

	enum { PROTOCOL_TYPE = 790442 };

	std::list<fire::pb::move::Pos> poslist; // 路径上的关键点
	fire::pb::move::Pos pos; // 位置
	int64_t sceneid;

	CRoleStop() {
		type = PROTOCOL_TYPE;
		sceneid = 0;
	}

	CRoleStop(const std::list<fire::pb::move::Pos> & _poslist_, const fire::pb::move::Pos & _pos_, int64_t _sceneid_)
		: poslist(_poslist_), pos(_pos_), sceneid(_sceneid_) {
		type = PROTOCOL_TYPE;
	}

	explicit CRoleStop(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::list<fire::pb::move::Pos>::const_iterator _i_ = poslist.begin(); _i_ != poslist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		if (!pos._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(poslist);
		_os_ << pos;
		_os_ << sceneid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(poslist);
		_os_ >> pos;
		_os_ >> sceneid;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CRoleStop(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
