	// Protocol

	enum { PROTOCOL_TYPE = 8076 };

	int paytype;
	int64_t orderid;
	int retcode;
	int userid;
	int64_t roleid;

	SysSendMail4_Re() {
		type = PROTOCOL_TYPE;
		paytype = 0;
		orderid = 0;
		retcode = 0;
		userid = 0;
		roleid = 0;
	}

	SysSendMail4_Re(int _paytype_, int64_t _orderid_, int _retcode_, int _userid_, int64_t _roleid_)
		: paytype(_paytype_), orderid(_orderid_), retcode(_retcode_), userid(_userid_), roleid(_roleid_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail4_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << paytype;
		_os_ << orderid;
		_os_ << retcode;
		_os_ << userid;
		_os_ << roleid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> paytype;
		_os_ >> orderid;
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> roleid;
		return _os_;
	}

	bool operator<(const SysSendMail4_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SysSendMail4_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = paytype - _o_.paytype;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(orderid - _o_.orderid);
		if (0 != _c_) return _c_;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail4_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
