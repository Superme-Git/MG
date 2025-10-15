	// Protocol

	enum { PROTOCOL_TYPE = 122 };

	int errcode;
	GNET::Octets info;
	int localsid;

	ErrorInfoRep() {
		type = PROTOCOL_TYPE;
		errcode = 0;
		localsid = 0;
	}

	ErrorInfoRep(int _errcode_, const GNET::Octets & _info_, int _localsid_)
		: errcode(_errcode_), info(_info_), localsid(_localsid_) {
		type = PROTOCOL_TYPE;
	}

	explicit ErrorInfoRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << errcode;
		_os_ << info;
		_os_ << localsid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> errcode;
		_os_ >> info;
		_os_ >> localsid;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new ErrorInfoRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
