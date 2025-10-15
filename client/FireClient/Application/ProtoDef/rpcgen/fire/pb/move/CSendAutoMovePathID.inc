	// Protocol

	enum { PROTOCOL_TYPE = 790492 };

	int pathid; // 巡游ID
	int pathid2; // 路线ID
	int pathid3; // 路线id3 by changhao

	CSendAutoMovePathID() {
		type = PROTOCOL_TYPE;
		pathid = 0;
		pathid2 = 0;
		pathid3 = 0;
	}

	CSendAutoMovePathID(int _pathid_, int _pathid2_, int _pathid3_)
		: pathid(_pathid_), pathid2(_pathid2_), pathid3(_pathid3_) {
		type = PROTOCOL_TYPE;
	}

	explicit CSendAutoMovePathID(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << pathid;
		_os_ << pathid2;
		_os_ << pathid3;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> pathid;
		_os_ >> pathid2;
		_os_ >> pathid3;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CSendAutoMovePathID &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CSendAutoMovePathID &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = pathid - _o_.pathid;
		if (0 != _c_) return _c_;
		_c_ = pathid2 - _o_.pathid2;
		if (0 != _c_) return _c_;
		_c_ = pathid3 - _o_.pathid3;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CSendAutoMovePathID(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
