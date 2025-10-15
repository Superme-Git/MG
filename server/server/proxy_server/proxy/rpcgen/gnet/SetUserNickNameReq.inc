	// Protocol

	enum { PROTOCOL_TYPE = 222 };

	int userid;
	GNET::Octets nickname;
	int reserved;

	SetUserNickNameReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
		reserved = 0;
	}

	SetUserNickNameReq(int _userid_, const GNET::Octets & _nickname_, int _reserved_)
		: userid(_userid_), nickname(_nickname_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit SetUserNickNameReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << nickname;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> nickname;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SetUserNickNameReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
