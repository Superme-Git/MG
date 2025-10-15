	// Protocol

	enum { PROTOCOL_TYPE = 110 };

	int userid;
	int localsid;
	int remain_time;
	int zoneid;
	int aid;
	int algorithm; // 赋值为algorithm，用于在client显示帐号安全等级

	OnlineAnnounce() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		remain_time = 0;
		zoneid = 0;
		aid = 0;
		algorithm = 0;
	}

	OnlineAnnounce(int _userid_, int _localsid_, int _remain_time_, int _zoneid_, int _aid_, int _algorithm_)
		: userid(_userid_), localsid(_localsid_), remain_time(_remain_time_), zoneid(_zoneid_), aid(_aid_), algorithm(_algorithm_) {
		type = PROTOCOL_TYPE;
	}

	explicit OnlineAnnounce(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << remain_time;
		_os_ << zoneid;
		_os_ << aid;
		_os_ << algorithm;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> remain_time;
		_os_ >> zoneid;
		_os_ >> aid;
		_os_ >> algorithm;
		return _os_;
	}

	bool operator<(const OnlineAnnounce &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const OnlineAnnounce &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = remain_time - _o_.remain_time;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = aid - _o_.aid;
		if (0 != _c_) return _c_;
		_c_ = algorithm - _o_.algorithm;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new OnlineAnnounce(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
