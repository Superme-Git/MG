	// Protocol

	enum { PROTOCOL_TYPE = 116 };

	gnet::UserLoginRes result;

	UserLoginRep() {
		type = PROTOCOL_TYPE;
	}

	explicit UserLoginRep(const gnet::UserLoginRes & _result_)
		: result(_result_) {
		type = PROTOCOL_TYPE;
	}

	explicit UserLoginRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!result._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << result;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> result;
		return _os_;
	}

	bool operator<(const UserLoginRep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLoginRep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = result.compareTo(_o_.result);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new UserLoginRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
