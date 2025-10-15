	// Protocol

	enum { PROTOCOL_TYPE = 857011 };

	int64_t roleid;
	int qtype;
	int seq;
	int reserved;
	std::vector<GNET::Octets> question;

	ACQuestion2() {
		type = PROTOCOL_TYPE;
		roleid = 0;
		qtype = 0;
		seq = 0;
		reserved = 0;
	}

	ACQuestion2(int64_t _roleid_, int _qtype_, int _seq_, int _reserved_, const std::vector<GNET::Octets> & _question_)
		: roleid(_roleid_), qtype(_qtype_), seq(_seq_), reserved(_reserved_), question(_question_) {
		type = PROTOCOL_TYPE;
	}

	ACQuestion2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << qtype;
		_os_ << seq;
		_os_ << reserved;
		_os_ << GNET::MarshalContainer(question);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> qtype;
		_os_ >> seq;
		_os_ >> reserved;
		_os_ >> GNET::MarshalContainer(question);
		return _os_;
	}

	aio::Protocol * Clone() const { return new ACQuestion2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
