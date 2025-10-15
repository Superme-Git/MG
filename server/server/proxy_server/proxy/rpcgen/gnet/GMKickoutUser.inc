	// Protocol

	enum { PROTOCOL_TYPE = 354 };

	int gmuserid;
	int localsid; // client在link的session id
	int kickuserid;
	int forbid_time; // time=1或2表示解封
	GNET::Octets reason;

	GMKickoutUser() {
		type = PROTOCOL_TYPE;
		gmuserid = 0;
		localsid = 0;
		kickuserid = 0;
		forbid_time = 600;
	}

	GMKickoutUser(int _gmuserid_, int _localsid_, int _kickuserid_, int _forbid_time_, const GNET::Octets & _reason_)
		: gmuserid(_gmuserid_), localsid(_localsid_), kickuserid(_kickuserid_), forbid_time(_forbid_time_), reason(_reason_) {
		type = PROTOCOL_TYPE;
	}

	explicit GMKickoutUser(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << gmuserid;
		_os_ << localsid;
		_os_ << kickuserid;
		_os_ << forbid_time;
		_os_ << reason;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> gmuserid;
		_os_ >> localsid;
		_os_ >> kickuserid;
		_os_ >> forbid_time;
		_os_ >> reason;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new GMKickoutUser(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
