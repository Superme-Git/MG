	// Protocol

	enum { PROTOCOL_TYPE = 119 };

	int userid;
	int localsid;
	gnet::RoleForbid forbid;

	AnnounceForbidInfo() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
	}

	AnnounceForbidInfo(int _userid_, int _localsid_, const gnet::RoleForbid & _forbid_)
		: userid(_userid_), localsid(_localsid_), forbid(_forbid_) {
		type = PROTOCOL_TYPE;
	}

	explicit AnnounceForbidInfo(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!forbid._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << forbid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> forbid;
		return _os_;
	}

	aio::Protocol * Clone() const { return new AnnounceForbidInfo(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 384; }
