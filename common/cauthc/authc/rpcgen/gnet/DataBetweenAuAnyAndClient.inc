	// Protocol

	enum { PROTOCOL_TYPE = 8921 };

	enum {
		AuAny_TO_CLIENT = 1,
		CLIENT_TO_AuAny = 2,
	};

	int userid;
	int qtype;
	char flag;
	FireNet::Octets info;
	int reserved;

	DataBetweenAuAnyAndClient() {
		type = PROTOCOL_TYPE;
		userid = 0;
		qtype = 0;
		flag = 0;
		reserved = 0;
	}

	DataBetweenAuAnyAndClient(int _userid_, int _qtype_, char _flag_, const FireNet::Octets & _info_, int _reserved_)
		: userid(_userid_), qtype(_qtype_), flag(_flag_), info(_info_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit DataBetweenAuAnyAndClient(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << qtype;
		_os_ << flag;
		_os_ << info;
		_os_ << reserved;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> qtype;
		_os_ >> flag;
		_os_ >> info;
		_os_ >> reserved;
		return _os_;
	}

	aio::Protocol * Clone() const { return new DataBetweenAuAnyAndClient(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
