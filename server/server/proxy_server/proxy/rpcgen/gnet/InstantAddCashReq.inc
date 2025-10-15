	// Protocol

	enum { PROTOCOL_TYPE = 208 };

	int userid;
	int localsid;
	GNET::Octets cardnum; // ≥‰÷µø®ø®∫≈
	GNET::Octets cardpasswd; // ≥‰÷µø®√‹¬Î
	int reserved;

	InstantAddCashReq() {
		type = PROTOCOL_TYPE;
		userid = 0;
		localsid = 0;
		reserved = 0;
	}

	InstantAddCashReq(int _userid_, int _localsid_, const GNET::Octets & _cardnum_, const GNET::Octets & _cardpasswd_, int _reserved_)
		: userid(_userid_), localsid(_localsid_), cardnum(_cardnum_), cardpasswd(_cardpasswd_), reserved(_reserved_) {
		type = PROTOCOL_TYPE;
	}

	explicit InstantAddCashReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << localsid;
		_os_ << cardnum;
		_os_ << cardpasswd;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> localsid;
		_os_ >> cardnum;
		_os_ >> cardpasswd;
		_os_ >> reserved;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new InstantAddCashReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
