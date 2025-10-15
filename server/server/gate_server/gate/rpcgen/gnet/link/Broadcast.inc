	// Protocol

	enum { PROTOCOL_TYPE = 65542 };

	int ptype;
	GNET::Octets pdata;
	int time;

	Broadcast() {
		type = PROTOCOL_TYPE;
		ptype = 0;
		time = 0;
	}

	Broadcast(int _ptype_, const GNET::Octets & _pdata_, int _time_)
		: ptype(_ptype_), pdata(_pdata_), time(_time_) {
		type = PROTOCOL_TYPE;
	}

	explicit Broadcast(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << ptype;
		_os_ << pdata;
		_os_ << time;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> ptype;
		_os_ >> pdata;
		_os_ >> time;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Broadcast(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
