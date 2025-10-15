	// Protocol

	enum { PROTOCOL_TYPE = 793439 };

	std::map<int, int> aiactions; // 战斗结束前需要播放的AIAction key=fighter id; value=aiaction id;

	SSendBattleEnd() {
		type = PROTOCOL_TYPE;
	}

	explicit SSendBattleEnd(const std::map<int, int> & _aiactions_)
		: aiactions(_aiactions_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendBattleEnd(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(aiactions);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(aiactions);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new SSendBattleEnd(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
