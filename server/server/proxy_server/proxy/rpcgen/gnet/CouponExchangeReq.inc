	// Protocol

	enum { PROTOCOL_TYPE = 218 };

	int userid;
	int coupon_number; // 要兑换的点券数量
	int cash_number; // 兑换的数量
	int reserved1;
	int reserved2;

	CouponExchangeReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
		coupon_number = 0;
		cash_number = 0;
		reserved1 = 0;
		reserved2 = 0;
	}

	CouponExchangeReq(int _userid_, int _coupon_number_, int _cash_number_, int _reserved1_, int _reserved2_)
		: userid(_userid_), coupon_number(_coupon_number_), cash_number(_cash_number_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit CouponExchangeReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << coupon_number;
		_os_ << cash_number;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> coupon_number;
		_os_ >> cash_number;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	bool operator<(const CouponExchangeReq &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CouponExchangeReq &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = coupon_number - _o_.coupon_number;
		if (0 != _c_) return _c_;
		_c_ = cash_number - _o_.cash_number;
		if (0 != _c_) return _c_;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		_c_ = reserved2 - _o_.reserved2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new CouponExchangeReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
