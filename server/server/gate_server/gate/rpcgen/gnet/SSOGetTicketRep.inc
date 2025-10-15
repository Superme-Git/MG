	// Protocol

	enum { PROTOCOL_TYPE = 8017 };

	int retcode;
	gnet::SSOUser user;
	GNET::Octets ticket;
	GNET::Octets local_context;
	GNET::Octets reserved;

	SSOGetTicketRep() {
		type = PROTOCOL_TYPE;
		retcode = 0;
	}

	SSOGetTicketRep(int _retcode_, const gnet::SSOUser & _user_, const GNET::Octets & _ticket_, const GNET::Octets & _local_context_, const GNET::Octets & _reserved_)
		: retcode(_retcode_), user(_user_), ticket(_ticket_), local_context(_local_context_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSOGetTicketRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!user._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << user;
		_os_ << ticket;
		_os_ << local_context;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> user;
		_os_ >> ticket;
		_os_ >> local_context;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SSOGetTicketRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 2048; }
