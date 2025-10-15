	// Protocol

	enum { PROTOCOL_TYPE = 8017 };

	int retcode;
	gnet::SSOUser user;
	FireNet::Octets ticket;
	FireNet::Octets local_context;
	FireNet::Octets reserved;

	SSOGetTicketRep() {
		type = PROTOCOL_TYPE;
		retcode = 0;
	}

	SSOGetTicketRep(int _retcode_, const gnet::SSOUser & _user_, const FireNet::Octets & _ticket_, const FireNet::Octets & _local_context_, const FireNet::Octets & _reserved_)
		: retcode(_retcode_), user(_user_), ticket(_ticket_), local_context(_local_context_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSOGetTicketRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!user._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << user;
		_os_ << ticket;
		_os_ << local_context;
		_os_ << reserved;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> user;
		_os_ >> ticket;
		_os_ >> local_context;
		_os_ >> reserved;
		return _os_;
	}

	aio::Protocol * Clone() const { return new SSOGetTicketRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 2048; }
