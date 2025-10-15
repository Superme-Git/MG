	// Protocol

	enum { PROTOCOL_TYPE = 793436 };

	fire::pb::battle::Operation action; // 操作
	char isrole; // 角色操作=1，宠物操作=0
	char autooperate; // 为1代表是自动，默认为0

	CSendAction() {
		type = PROTOCOL_TYPE;
		isrole = 0;
		autooperate = 0;
	}

	CSendAction(const fire::pb::battle::Operation & _action_, char _isrole_, char _autooperate_)
		: action(_action_), isrole(_isrole_), autooperate(_autooperate_) {
		type = PROTOCOL_TYPE;
	}

	explicit CSendAction(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!action._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << action;
		_os_ << isrole;
		_os_ << autooperate;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> action;
		_os_ >> isrole;
		_os_ >> autooperate;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CSendAction &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CSendAction &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = action.compareTo(_o_.action);
		if (0 != _c_) return _c_;
		_c_ = isrole - _o_.isrole;
		if (0 != _c_) return _c_;
		_c_ = autooperate - _o_.autooperate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CSendAction(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
