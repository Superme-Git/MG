	// Protocol

	enum { PROTOCOL_TYPE = 8902 };

	int plattype; // 当前服务器组，登录的是那个平台
	GNET::Octets serverid; // 当前服务器组的标识

	ServerIDResponse() {
		type = PROTOCOL_TYPE;
		plattype = 0;
	}

	ServerIDResponse(int _plattype_, const GNET::Octets & _serverid_)
		: plattype(_plattype_), serverid(_serverid_) {
		type = PROTOCOL_TYPE;
	}

	explicit ServerIDResponse(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << plattype;
		_os_ << serverid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> plattype;
		_os_ >> serverid;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new ServerIDResponse(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
