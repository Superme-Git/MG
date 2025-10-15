	// Protocol

	enum { PROTOCOL_TYPE = 114 };

	int localsid;
	int retcode;
	int userid;
	GNET::Octets response;
	char blmatrixchallenge;
	int algorithm;
	int nonce;

	MatrixPasswdRep() {
		type = PROTOCOL_TYPE;
		localsid = 0;
		retcode = 0;
		userid = 0;
		blmatrixchallenge = 0;
		algorithm = 0;
		nonce = 0;
	}

	MatrixPasswdRep(int _localsid_, int _retcode_, int _userid_, const GNET::Octets & _response_, char _blmatrixchallenge_, int _algorithm_, int _nonce_)
		: localsid(_localsid_), retcode(_retcode_), userid(_userid_), response(_response_), blmatrixchallenge(_blmatrixchallenge_), algorithm(_algorithm_), nonce(_nonce_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixPasswdRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << localsid;
		_os_ << retcode;
		_os_ << userid;
		_os_ << response;
		_os_ << blmatrixchallenge;
		_os_ << algorithm;
		_os_ << nonce;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> localsid;
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> response;
		_os_ >> blmatrixchallenge;
		_os_ >> algorithm;
		_os_ >> nonce;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new MatrixPasswdRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
