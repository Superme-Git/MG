	// Protocol

	enum { PROTOCOL_TYPE = 515 };

	int userid;
	int zoneid;
	int sn;
	int cash;

	AddCash() {
		type = PROTOCOL_TYPE;
		userid = 0;
		zoneid = -1;
		sn = 0;
		cash = 0;
	}

	AddCash(int _userid_, int _zoneid_, int _sn_, int _cash_)
		: userid(_userid_), zoneid(_zoneid_), sn(_sn_), cash(_cash_) {
		type = PROTOCOL_TYPE;
	}

	explicit AddCash(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << zoneid;
		_os_ << sn;
		_os_ << cash;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> zoneid;
		_os_ >> sn;
		_os_ >> cash;
		return _os_;
	}

	bool operator<(const AddCash &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AddCash &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = sn - _o_.sn;
		if (0 != _c_) return _c_;
		_c_ = cash - _o_.cash;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AddCash(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
