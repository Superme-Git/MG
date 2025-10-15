	// Protocol

	enum { PROTOCOL_TYPE = 65543 };

	enum {
		eLogout = 0,
		eLogin = 1,
	};

	int linksid;
	int login;
	int64_t roleid; // for debug

	SetLogin() {
		type = PROTOCOL_TYPE;
		linksid = 0;
		login = 0;
		roleid = 0;
	}

	SetLogin(int _linksid_, int _login_, int64_t _roleid_)
		: linksid(_linksid_), login(_login_), roleid(_roleid_) {
		type = PROTOCOL_TYPE;
	}

	explicit SetLogin(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << linksid;
		_os_ << login;
		_os_ << roleid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> linksid;
		_os_ >> login;
		_os_ >> roleid;
		return _os_;
	}

	bool operator<(const SetLogin &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SetLogin &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = linksid - _o_.linksid;
		if (0 != _c_) return _c_;
		_c_ = login - _o_.login;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new SetLogin(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
