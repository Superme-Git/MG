	// Protocol

	enum { PROTOCOL_TYPE = 786514 };

	std::vector<fire::pb::UserInfoUnit> info;

	CNotifyDeviceInfo() {
		type = PROTOCOL_TYPE;
	}

	explicit CNotifyDeviceInfo(const std::vector<fire::pb::UserInfoUnit> & _info_)
		: info(_info_) {
		type = PROTOCOL_TYPE;
	}

	explicit CNotifyDeviceInfo(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<fire::pb::UserInfoUnit>::const_iterator _i_ = info.begin(); _i_ != info.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(info);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(info);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new CNotifyDeviceInfo(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
