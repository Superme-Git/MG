	// Protocol

	enum { PROTOCOL_TYPE = 102 };

	int errcode;
	GNET::Octets info;

	ErrorInfo() {
		type = PROTOCOL_TYPE;
		errcode = 0;
	}

	ErrorInfo(int _errcode_, const GNET::Octets & _info_)
		: errcode(_errcode_), info(_info_) {
		type = PROTOCOL_TYPE;
	}

	explicit ErrorInfo(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << errcode;
		_os_ << info;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> errcode;
		_os_ >> info;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new ErrorInfo(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
