	// Protocol

	enum { PROTOCOL_TYPE = 219 };

	int retcode;
	int userid;
	int remain_coupon; // 点券余额
	int today_exchange_coupon; // 今天已使用的点券
	int today_remain_exchange_coupon; // 今天还可使用的点券
	int reserved1;
	int reserved2;

	CouponExchangeRep() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		userid = 0;
		remain_coupon = 0;
		today_exchange_coupon = 0;
		today_remain_exchange_coupon = 0;
		reserved1 = 0;
		reserved2 = 0;
	}

	CouponExchangeRep(int _retcode_, int _userid_, int _remain_coupon_, int _today_exchange_coupon_, int _today_remain_exchange_coupon_, int _reserved1_, int _reserved2_)
		: retcode(_retcode_), userid(_userid_), remain_coupon(_remain_coupon_), today_exchange_coupon(_today_exchange_coupon_), today_remain_exchange_coupon(_today_remain_exchange_coupon_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit CouponExchangeRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << remain_coupon;
		_os_ << today_exchange_coupon;
		_os_ << today_remain_exchange_coupon;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> remain_coupon;
		_os_ >> today_exchange_coupon;
		_os_ >> today_remain_exchange_coupon;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	bool operator<(const CouponExchangeRep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CouponExchangeRep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = remain_coupon - _o_.remain_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_exchange_coupon - _o_.today_exchange_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_remain_exchange_coupon - _o_.today_remain_exchange_coupon;
		if (0 != _c_) return _c_;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		_c_ = reserved2 - _o_.reserved2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CouponExchangeRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
