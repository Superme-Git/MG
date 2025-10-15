	// Protocol

	enum { PROTOCOL_TYPE = 356 };

	int gmuserid;
	int localsid;
	int dstuserid;
	int forbid_time;
	GNET::Octets reason;

	GMShutup() {
		type = PROTOCOL_TYPE;
		gmuserid = 0;
		localsid = 0;
		dstuserid = 0;
		forbid_time = 0;
	}

	GMShutup(int _gmuserid_, int _localsid_, int _dstuserid_, int _forbid_time_, const GNET::Octets & _reason_)
		: gmuserid(_gmuserid_), localsid(_localsid_), dstuserid(_dstuserid_), forbid_time(_forbid_time_), reason(_reason_) {
		type = PROTOCOL_TYPE;
	}

	explicit GMShutup(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << gmuserid;
		_os_ << localsid;
		_os_ << dstuserid;
		_os_ << forbid_time;
		_os_ << reason;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> gmuserid;
		_os_ >> localsid;
		_os_ >> dstuserid;
		_os_ >> forbid_time;
		_os_ >> reason;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new GMShutup(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
