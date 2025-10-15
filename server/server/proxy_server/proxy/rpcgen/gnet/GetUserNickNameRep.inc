	// Protocol

	enum { PROTOCOL_TYPE = 221 };

	int retcode;
	int userid;
	GNET::Octets nickname;
	int reserved;

	GetUserNickNameRep() {
		type = PROTOCOL_TYPE;
		retcode = 0;
		userid = 0;
		reserved = 0;
	}

	GetUserNickNameRep(int _retcode_, int _userid_, const GNET::Octets & _nickname_, int _reserved_)
		: retcode(_retcode_), userid(_userid_), nickname(_nickname_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit GetUserNickNameRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << nickname;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> nickname;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new GetUserNickNameRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
