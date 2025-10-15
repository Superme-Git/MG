	// Protocol

	enum { PROTOCOL_TYPE = 543 };

	int userid;
	int zoneid;

	QueryUserPrivilege3() {
		type = PROTOCOL_TYPE;
		userid = 0;
		zoneid = 0;
	}

	QueryUserPrivilege3(int _userid_, int _zoneid_)
		: userid(_userid_), zoneid(_zoneid_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryUserPrivilege3(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << zoneid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> zoneid;
		return _os_;
	}

	bool operator<(const QueryUserPrivilege3 &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const QueryUserPrivilege3 &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new QueryUserPrivilege3(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
