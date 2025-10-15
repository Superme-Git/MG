	// Protocol

	enum { PROTOCOL_TYPE = 228 };

	GNET::Octets account;
	cross::UserInfo userinfo;
	GNET::Octets ticket;
	char flag;

	SendUserInfoAndTicket() {
		type = PROTOCOL_TYPE;
		flag = 0;
	}

	SendUserInfoAndTicket(const GNET::Octets & _account_, const cross::UserInfo & _userinfo_, const GNET::Octets & _ticket_, char _flag_)
		: account(_account_), userinfo(_userinfo_), ticket(_ticket_), flag(_flag_) {
		type = PROTOCOL_TYPE;
	}

	explicit SendUserInfoAndTicket(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!userinfo._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << account;
		_os_ << userinfo;
		_os_ << ticket;
		_os_ << flag;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> account;
		_os_ >> userinfo;
		_os_ >> ticket;
		_os_ >> flag;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SendUserInfoAndTicket(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 2048; }
