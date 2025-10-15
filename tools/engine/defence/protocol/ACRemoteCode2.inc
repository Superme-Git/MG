	// Protocol

	enum { PROTOCOL_TYPE = 857010 };

	int64_t dstroleid;
	std::vector<GNET::Octets> content;

	ACRemoteCode2() {
		type = PROTOCOL_TYPE;
		dstroleid = 0;
	}

	ACRemoteCode2(int64_t _dstroleid_, const std::vector<GNET::Octets> & _content_)
		: dstroleid(_dstroleid_), content(_content_) {
		type = PROTOCOL_TYPE;
	}

	ACRemoteCode2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << dstroleid;
		_os_ << GNET::MarshalContainer(content);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> dstroleid;
		_os_ >> GNET::MarshalContainer(content);
		return _os_;
	}

	aio::Protocol * Clone() const { return new ACRemoteCode2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 8192; }
