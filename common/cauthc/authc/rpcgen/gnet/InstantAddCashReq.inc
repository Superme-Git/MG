	// Protocol

	enum { PROTOCOL_TYPE = 208 };

	int userid;
	int localsid;
	FireNet::Octets cardnum; // ≥‰÷µø®ø®∫≈
	FireNet::Octets cardpasswd; // ≥‰÷µø®√‹¬Î
	int reserved;

	InstantAddCashReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		reserved = 0;
	}

	InstantAddCashReq(int _userid_, int _localsid_, const FireNet::Octets & _cardnum_, const FireNet::Octets & _cardpasswd_, int _reserved_)
		: userid(_userid_), localsid(_localsid_), cardnum(_cardnum_), cardpasswd(_cardpasswd_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit InstantAddCashReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << cardnum;
		_os_ << cardpasswd;
		_os_ << reserved;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> cardnum;
		_os_ >> cardpasswd;
		_os_ >> reserved;
		return _os_;
	}

	aio::Protocol * Clone() const { return new InstantAddCashReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
