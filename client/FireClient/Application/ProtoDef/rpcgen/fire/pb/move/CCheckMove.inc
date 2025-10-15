	// Protocol

	enum { PROTOCOL_TYPE = 790435 };

	fire::pb::move::Pos curpos; // 人物当前点
	std::list<fire::pb::move::Pos> poslist; // 路径上的关键点
	int64_t sceneid; // 场景id

	CCheckMove() {
		type = PROTOCOL_TYPE;
		sceneid = 0;
	}

	CCheckMove(const fire::pb::move::Pos & _curpos_, const std::list<fire::pb::move::Pos> & _poslist_, int64_t _sceneid_)
		: curpos(_curpos_), poslist(_poslist_), sceneid(_sceneid_) {
		type = PROTOCOL_TYPE;
	}

	explicit CCheckMove(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!curpos._validator_()) return false;
		for (std::list<fire::pb::move::Pos>::const_iterator _i_ = poslist.begin(); _i_ != poslist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << curpos;
		_os_ << FireNet::MarshalContainer(poslist);
		_os_ << sceneid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> curpos;
		_os_ >> FireNet::MarshalContainer(poslist);
		_os_ >> sceneid;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CCheckMove(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
