	// Protocol

	enum { PROTOCOL_TYPE = 857013 };

	int64_t roleid;
	int pptype;
	int seq;
	int key;

	ACPP() {
		type = PROTOCOL_TYPE;
		roleid = 0;
		pptype = 0;
		seq = 0;
		key = 0;
	}

	ACPP(int64_t _roleid_, int _pptype_, int _seq_, int _key_)
		: roleid(_roleid_), pptype(_pptype_), seq(_seq_), key(_key_) {
		type = PROTOCOL_TYPE;
	}

	ACPP(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << pptype;
		_os_ << seq;
		_os_ << key;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> pptype;
		_os_ >> seq;
		_os_ >> key;
		return _os_;
	}

	bool operator<(const ACPP &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ACPP &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = pptype - _o_.pptype;
		if (0 != _c_) return _c_;
		_c_ = seq - _o_.seq;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new ACPP(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
