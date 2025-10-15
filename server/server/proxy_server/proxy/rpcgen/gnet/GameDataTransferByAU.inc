	// Protocol

	enum { PROTOCOL_TYPE = 8074 };

	int fromzoneid;
	int tozoneid;
	int flag;
	GNET::Octets data;
	int reserved1;
	GNET::Octets reserved2;

	GameDataTransferByAU() {
		type = PROTOCOL_TYPE;
		fromzoneid = 0;
		tozoneid = 0;
		flag = 0;
		reserved1 = 0;
	}

	GameDataTransferByAU(int _fromzoneid_, int _tozoneid_, int _flag_, const GNET::Octets & _data_, int _reserved1_, const GNET::Octets & _reserved2_)
		: fromzoneid(_fromzoneid_), tozoneid(_tozoneid_), flag(_flag_), data(_data_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit GameDataTransferByAU(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << fromzoneid;
		_os_ << tozoneid;
		_os_ << flag;
		_os_ << data;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> fromzoneid;
		_os_ >> tozoneid;
		_os_ >> flag;
		_os_ >> data;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new GameDataTransferByAU(*this); }
	int  PriorPolicy( ) const { return 3; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
