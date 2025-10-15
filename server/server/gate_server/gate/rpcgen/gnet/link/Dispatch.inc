	// Protocol

	enum { PROTOCOL_TYPE = 65541 };

	int linksid;
	int userid;
	int ptype;
	GNET::Octets pdata;

	Dispatch() {
		type = PROTOCOL_TYPE;
		linksid = 0;
		userid = 0;
		ptype = 0;
	}

	Dispatch(int _linksid_, int _userid_, int _ptype_, const GNET::Octets & _pdata_)
		: linksid(_linksid_), userid(_userid_), ptype(_ptype_), pdata(_pdata_) {
		type = PROTOCOL_TYPE;
	}

	explicit Dispatch(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << linksid;
		_os_ << userid;
		_os_ << ptype;
		_os_ << pdata;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> linksid;
		_os_ >> userid;
		_os_ >> ptype;
		_os_ >> pdata;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Dispatch(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
