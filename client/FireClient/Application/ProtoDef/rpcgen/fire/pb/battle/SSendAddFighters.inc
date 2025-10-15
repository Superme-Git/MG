	// Protocol

	enum { PROTOCOL_TYPE = 793434 };

	std::list<fire::pb::battle::FighterInfo> fighterlist;

	SSendAddFighters() {
		type = PROTOCOL_TYPE;
	}

	explicit SSendAddFighters(const std::list<fire::pb::battle::FighterInfo> & _fighterlist_)
		: fighterlist(_fighterlist_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendAddFighters(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::list<fire::pb::battle::FighterInfo>::const_iterator _i_ = fighterlist.begin(); _i_ != fighterlist.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(fighterlist);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(fighterlist);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new SSendAddFighters(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
