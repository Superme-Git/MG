	// Protocol

	enum { PROTOCOL_TYPE = 8016 };

	gnet::SSOUser user;
	int loginip;
	int toaid;
	int tozoneid;
	GNET::Octets info;
	GNET::Octets local_context;
	GNET::Octets server_context;
	GNET::Octets reserved;

	SSOGetTicketReq() {
		type = PROTOCOL_TYPE;
		loginip = 0;
		toaid = 0;
		tozoneid = 0;
	}

	SSOGetTicketReq(const gnet::SSOUser & _user_, int _loginip_, int _toaid_, int _tozoneid_, const GNET::Octets & _info_, const GNET::Octets & _local_context_, const GNET::Octets & _server_context_, const GNET::Octets & _reserved_)
		: user(_user_), loginip(_loginip_), toaid(_toaid_), tozoneid(_tozoneid_), info(_info_), local_context(_local_context_), server_context(_server_context_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSOGetTicketReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!user._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << user;
		_os_ << loginip;
		_os_ << toaid;
		_os_ << tozoneid;
		_os_ << info;
		_os_ << local_context;
		_os_ << server_context;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> user;
		_os_ >> loginip;
		_os_ >> toaid;
		_os_ >> tozoneid;
		_os_ >> info;
		_os_ >> local_context;
		_os_ >> server_context;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SSOGetTicketReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 2048; }
