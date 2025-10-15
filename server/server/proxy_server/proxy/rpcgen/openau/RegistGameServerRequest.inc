	// Protocol

	enum { PROTOCOL_TYPE = 8900 };

	int plattype; // 如果是 unknow 就由 aunay 决定登录哪个平台
	GNET::Octets serverid; // 当前服务器组的标识 delivery 的配置中指定
	int ip1;
	int ip2;
	int ip3;

	RegistGameServerRequest() {
		type = PROTOCOL_TYPE;
		plattype = AuAnyPlatTypes::unknow;
		ip1 = 0;
		ip2 = 0;
		ip3 = 0;
	}

	RegistGameServerRequest(int _plattype_, const GNET::Octets & _serverid_, int _ip1_, int _ip2_, int _ip3_)
		: plattype(_plattype_), serverid(_serverid_), ip1(_ip1_), ip2(_ip2_), ip3(_ip3_) {
		type = PROTOCOL_TYPE;
	}

	explicit RegistGameServerRequest(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << plattype;
		_os_ << serverid;
		_os_ << ip1;
		_os_ << ip2;
		_os_ << ip3;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> plattype;
		_os_ >> serverid;
		_os_ >> ip1;
		_os_ >> ip2;
		_os_ >> ip3;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new RegistGameServerRequest(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
