	// Protocol

	enum { PROTOCOL_TYPE = 8045 };

	int userid;
	int aid;
	int zoneid;
	int currenttime; // vip服务器的时间
	int starttime; // vip的起始时间
	int endtime; // vip的结束时间
	int status; // vip状态
	int viplevel; // vip等级
	int totalcash; // 充值总额,单位元
	int recenttotalcash; // 近期充值总额,单位元
	int infolack; // 账号的vip信息是否不全
	int reserved;

	VIPAnnounce() {
		type = PROTOCOL_TYPE;
		userid = 0;
		aid = 0;
		zoneid = 0;
		currenttime = 0;
		starttime = 0;
		endtime = 0;
		status = 0;
		viplevel = 0;
		totalcash = 0;
		recenttotalcash = 0;
		infolack = 0;
		reserved = 0;
	}

	VIPAnnounce(int _userid_, int _aid_, int _zoneid_, int _currenttime_, int _starttime_, int _endtime_, int _status_, int _viplevel_, int _totalcash_, int _recenttotalcash_, int _infolack_, int _reserved_)
		: userid(_userid_), aid(_aid_), zoneid(_zoneid_), currenttime(_currenttime_), starttime(_starttime_), endtime(_endtime_), status(_status_), viplevel(_viplevel_), totalcash(_totalcash_), recenttotalcash(_recenttotalcash_), infolack(_infolack_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit VIPAnnounce(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << aid;
		_os_ << zoneid;
		_os_ << currenttime;
		_os_ << starttime;
		_os_ << endtime;
		_os_ << status;
		_os_ << viplevel;
		_os_ << totalcash;
		_os_ << recenttotalcash;
		_os_ << infolack;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> aid;
		_os_ >> zoneid;
		_os_ >> currenttime;
		_os_ >> starttime;
		_os_ >> endtime;
		_os_ >> status;
		_os_ >> viplevel;
		_os_ >> totalcash;
		_os_ >> recenttotalcash;
		_os_ >> infolack;
		_os_ >> reserved;
		return _os_;
	}

	bool operator<(const VIPAnnounce &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const VIPAnnounce &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = aid - _o_.aid;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = currenttime - _o_.currenttime;
		if (0 != _c_) return _c_;
		_c_ = starttime - _o_.starttime;
		if (0 != _c_) return _c_;
		_c_ = endtime - _o_.endtime;
		if (0 != _c_) return _c_;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		_c_ = viplevel - _o_.viplevel;
		if (0 != _c_) return _c_;
		_c_ = totalcash - _o_.totalcash;
		if (0 != _c_) return _c_;
		_c_ = recenttotalcash - _o_.recenttotalcash;
		if (0 != _c_) return _c_;
		_c_ = infolack - _o_.infolack;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new VIPAnnounce(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
