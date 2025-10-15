	// Protocol

	enum { PROTOCOL_TYPE = 8069 };

	int64_t orderid;
	int retcode;
	int64_t roleid;

	SysSendMail3_Re() {
		type = PROTOCOL_TYPE;
		orderid = 0;
		retcode = 0;
		roleid = 0;
	}

	SysSendMail3_Re(int64_t _orderid_, int _retcode_, int64_t _roleid_)
		: orderid(_orderid_), retcode(_retcode_), roleid(_roleid_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail3_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << orderid;
		_os_ << retcode;
		_os_ << roleid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> orderid;
		_os_ >> retcode;
		_os_ >> roleid;
		return _os_;
	}

	bool operator<(const SysSendMail3_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SysSendMail3_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = GNET::signum(orderid - _o_.orderid);
		if (0 != _c_) return _c_;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail3_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
