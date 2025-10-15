	// Protocol

	enum { PROTOCOL_TYPE = 65540 };

	std::set<int> linksids;
	int ptype;
	GNET::Octets pdata;

	Send() {
		type = PROTOCOL_TYPE;
		ptype = 0;
	}

	Send(const std::set<int> & _linksids_, int _ptype_, const GNET::Octets & _pdata_)
		: linksids(_linksids_), ptype(_ptype_), pdata(_pdata_) {
		type = PROTOCOL_TYPE;
	}

	explicit Send(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << GNET::MarshalContainer(linksids);
		_os_ << ptype;
		_os_ << pdata;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> GNET::MarshalContainer(linksids);
		_os_ >> ptype;
		_os_ >> pdata;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Send(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
