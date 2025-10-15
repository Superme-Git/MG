	// Protocol

	enum { PROTOCOL_TYPE = 105 };

	int userid;
	int localsid;
	int deny_flag; // 1 ¾Ü¾ø 0 ½ÓÊÜ±»Ìß
	int reserved;

	ForceLoginRep() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		deny_flag = 0;
		reserved = 0;
	}

	ForceLoginRep(int _userid_, int _localsid_, int _deny_flag_, int _reserved_)
		: userid(_userid_), localsid(_localsid_), deny_flag(_deny_flag_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit ForceLoginRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << deny_flag;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> deny_flag;
		_os_ >> reserved;
		return _os_;
	}

	bool operator<(const ForceLoginRep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ForceLoginRep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = deny_flag - _o_.deny_flag;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new ForceLoginRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
