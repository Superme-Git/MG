	// Protocol

	enum { PROTOCOL_TYPE = 230 };

	enum {
		TO_CROSS = 1,
		FROM_CROSS = 2,
	};

	int userid;
	int dtype;
	char flag;
	GNET::Octets content;

	UserDataTransfer() {
		type = PROTOCOL_TYPE;
		userid = 0;
		dtype = 0;
		flag = 0;
	}

	UserDataTransfer(int _userid_, int _dtype_, char _flag_, const GNET::Octets & _content_)
		: userid(_userid_), dtype(_dtype_), flag(_flag_), content(_content_) {
		type = PROTOCOL_TYPE;
	}

	explicit UserDataTransfer(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << dtype;
		_os_ << flag;
		_os_ << content;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> dtype;
		_os_ >> flag;
		_os_ >> content;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new UserDataTransfer(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
