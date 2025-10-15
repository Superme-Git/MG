	// Protocol

	enum { PROTOCOL_TYPE = 220 };

	int userid;

	GetUserNickNameReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
	}

	explicit GetUserNickNameReq(int _userid_)
		: userid(_userid_) {
		type = PROTOCOL_TYPE;
	}

	explicit GetUserNickNameReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		return _os_;
	}

	bool operator<(const GetUserNickNameReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetUserNickNameReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new GetUserNickNameReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
