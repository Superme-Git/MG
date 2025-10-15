	// Protocol

	enum { PROTOCOL_TYPE = 216 };

	int userid;

	GetUserCouponReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
	}

	explicit GetUserCouponReq(int _userid_)
		: userid(_userid_) {
		type = PROTOCOL_TYPE;
	}

	explicit GetUserCouponReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		return _os_;
	}

	bool operator<(const GetUserCouponReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetUserCouponReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new GetUserCouponReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
