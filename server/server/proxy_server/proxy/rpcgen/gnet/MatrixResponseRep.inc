	// Protocol

	enum { PROTOCOL_TYPE = 121 };

	char blkeyexchange;
	int localsid;

	MatrixResponseRep() {
		type = PROTOCOL_TYPE;
		blkeyexchange = 0;
		localsid = 0;
	}

	MatrixResponseRep(char _blkeyexchange_, int _localsid_)
		: blkeyexchange(_blkeyexchange_), localsid(_localsid_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixResponseRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << blkeyexchange;
		_os_ << localsid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> blkeyexchange;
		_os_ >> localsid;
		return _os_;
	}

	bool operator<(const MatrixResponseRep &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MatrixResponseRep &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = blkeyexchange - _o_.blkeyexchange;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	GNET::Protocol * Clone() const { return new MatrixResponseRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
