	// Protocol

	enum { PROTOCOL_TYPE = 102 };

	int errcode;
	FireNet::Octets info;

	ErrorInfo() {
		type = PROTOCOL_TYPE;
		errcode = 0;
	}

	ErrorInfo(int _errcode_, const FireNet::Octets & _info_)
		: errcode(_errcode_), info(_info_) {
		type = PROTOCOL_TYPE;
	}

	explicit ErrorInfo(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << errcode;
		_os_ << info;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> errcode;
		_os_ >> info;
		return _os_;
	}

	aio::Protocol * Clone() const { return new ErrorInfo(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
