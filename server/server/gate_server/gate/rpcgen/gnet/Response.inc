	// Protocol

	enum { PROTOCOL_TYPE = 103 };

	GNET::Octets identity; // 帐号
	GNET::Octets response; // 密码(使用平台sdk时发session)
	int logintype; // 登录类型(sdk类型) 1:locojoy platform sdk
	int gameid;
	GNET::Octets gamekey;
	GNET::Octets serverid;
	GNET::Octets selfchannel; // 渠道号
	GNET::Octets childchannel; // 子渠道号
	GNET::Octets deviceid; // 设备唯一标识
	GNET::Octets mid;
	int reserved1;
	GNET::Octets reserved2;

	Response() {
		type = PROTOCOL_TYPE;
		logintype = 0;
		gameid = 0;
		reserved1 = 0;
	}

	Response(const GNET::Octets & _identity_, const GNET::Octets & _response_, int _logintype_, int _gameid_, const GNET::Octets & _gamekey_, const GNET::Octets & _serverid_, const GNET::Octets & _selfchannel_, const GNET::Octets & _childchannel_, const GNET::Octets & _deviceid_, const GNET::Octets & _mid_, int _reserved1_, const GNET::Octets & _reserved2_)
		: identity(_identity_), response(_response_), logintype(_logintype_), gameid(_gameid_), gamekey(_gamekey_), serverid(_serverid_), selfchannel(_selfchannel_), childchannel(_childchannel_), deviceid(_deviceid_), mid(_mid_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit Response(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << identity;
		_os_ << response;
		_os_ << logintype;
		_os_ << gameid;
		_os_ << gamekey;
		_os_ << serverid;
		_os_ << selfchannel;
		_os_ << childchannel;
		_os_ << deviceid;
		_os_ << mid;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> identity;
		_os_ >> response;
		_os_ >> logintype;
		_os_ >> gameid;
		_os_ >> gamekey;
		_os_ >> serverid;
		_os_ >> selfchannel;
		_os_ >> childchannel;
		_os_ >> deviceid;
		_os_ >> mid;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new Response(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
