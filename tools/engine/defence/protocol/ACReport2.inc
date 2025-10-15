	// Protocol

	enum { PROTOCOL_TYPE = 857009 };

	int64_t roleid;
	GNET::Octets report;

	ACReport2() {
		type = PROTOCOL_TYPE;
		roleid = 0;
	}

	ACReport2(int64_t _roleid_, const GNET::Octets & _report_)
		: roleid(_roleid_), report(_report_) {
		type = PROTOCOL_TYPE;
	}

	ACReport2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << report;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> report;
		return _os_;
	}

	aio::Protocol * Clone() const { return new ACReport2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
