	// Protocol

	enum { PROTOCOL_TYPE = 8038 };

	int userid;
	int qtype;
	GNET::Octets info;
	int retcode;
	int reserved;

	AU2Game() {
		type = PROTOCOL_TYPE;
		userid = 0;
		qtype = 0;
		retcode = 0;
		reserved = 0;
	}

	AU2Game(int _userid_, int _qtype_, const GNET::Octets & _info_, int _retcode_, int _reserved_)
		: userid(_userid_), qtype(_qtype_), info(_info_), retcode(_retcode_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit AU2Game(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << qtype;
		_os_ << info;
		_os_ << retcode;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> qtype;
		_os_ >> info;
		_os_ >> retcode;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new AU2Game(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
