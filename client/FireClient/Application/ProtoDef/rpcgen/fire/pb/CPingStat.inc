	// Protocol

	enum { PROTOCOL_TYPE = 786490 };

	int64_t beginstamp; // 开始时间戳
	fire::pb::PingStatEntry pingstats; // 10分钟内统计数据
	short losspercent; // 丢包率（losspercent%100）

	CPingStat() {
		type = PROTOCOL_TYPE;
		beginstamp = 0;
		losspercent = 0;
	}

	CPingStat(int64_t _beginstamp_, const fire::pb::PingStatEntry & _pingstats_, short _losspercent_)
		: beginstamp(_beginstamp_), pingstats(_pingstats_), losspercent(_losspercent_) {
		type = PROTOCOL_TYPE;
	}

	explicit CPingStat(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!pingstats._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << beginstamp;
		_os_ << pingstats;
		_os_ << losspercent;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> beginstamp;
		_os_ >> pingstats;
		_os_ >> losspercent;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CPingStat &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CPingStat &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = FireNet::signum(beginstamp - _o_.beginstamp);
		if (0 != _c_) return _c_;
		_c_ = pingstats.compareTo(_o_.pingstats);
		if (0 != _c_) return _c_;
		_c_ = losspercent - _o_.losspercent;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CPingStat(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
