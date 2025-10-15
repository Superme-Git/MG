	// Protocol

	enum { PROTOCOL_TYPE = 857012 };

	int64_t roleid;
	int qtype;
	int seq;
	int reserved;
	int answer;

	ACAnswer2() {
		type = PROTOCOL_TYPE;
		roleid = 0;
		qtype = 0;
		seq = 0;
		reserved = 0;
		answer = 0;
	}

	ACAnswer2(int64_t _roleid_, int _qtype_, int _seq_, int _reserved_, int _answer_)
		: roleid(_roleid_), qtype(_qtype_), seq(_seq_), reserved(_reserved_), answer(_answer_) {
		type = PROTOCOL_TYPE;
	}

	ACAnswer2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << qtype;
		_os_ << seq;
		_os_ << reserved;
		_os_ << answer;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> qtype;
		_os_ >> seq;
		_os_ >> reserved;
		_os_ >> answer;
		return _os_;
	}

	bool operator<(const ACAnswer2 &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ACAnswer2 &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = qtype - _o_.qtype;
		if (0 != _c_) return _c_;
		_c_ = seq - _o_.seq;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new ACAnswer2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
