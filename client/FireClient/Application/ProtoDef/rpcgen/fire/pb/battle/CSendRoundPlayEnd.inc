	// Protocol

	enum { PROTOCOL_TYPE = 793438 };

	std::list<int> actiontime; // 每个动作的时间，不包括动作间填充时间0.2秒

	CSendRoundPlayEnd() {
		type = PROTOCOL_TYPE;
	}

	explicit CSendRoundPlayEnd(const std::list<int> & _actiontime_)
		: actiontime(_actiontime_) {
		type = PROTOCOL_TYPE;
	}

	explicit CSendRoundPlayEnd(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(actiontime);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(actiontime);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CSendRoundPlayEnd(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
