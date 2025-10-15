	// Protocol

	enum { PROTOCOL_TYPE = 516 };

	int retcode;
	int userid;
	int zoneid;
	int sn;

	AddCash_Re() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		userid = -1;
		zoneid = -1;
		sn = 0;
	}

	AddCash_Re(int _retcode_, int _userid_, int _zoneid_, int _sn_)
		: retcode(_retcode_), userid(_userid_), zoneid(_zoneid_), sn(_sn_) {
		type = PROTOCOL_TYPE;
	}

	explicit AddCash_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << zoneid;
		_os_ << sn;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> zoneid;
		_os_ >> sn;
		return _os_;
	}

	bool operator<(const AddCash_Re &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AddCash_Re &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = sn - _o_.sn;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new AddCash_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
