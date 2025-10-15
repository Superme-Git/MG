	// Protocol

	enum { PROTOCOL_TYPE = 65537 };

	int pvid; // provider id
	std::set<int> linksids; // bind default if empty

	Bind() {
		type = PROTOCOL_TYPE;
		pvid = 0;
	}

	Bind(int _pvid_, const std::set<int> & _linksids_)
		: pvid(_pvid_), linksids(_linksids_) {
		type = PROTOCOL_TYPE;
	}

	explicit Bind(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << pvid;
		_os_ << GNET::MarshalContainer(linksids);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> pvid;
		_os_ >> GNET::MarshalContainer(linksids);
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Bind(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
