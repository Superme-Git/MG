	// Protocol

	enum { PROTOCOL_TYPE = 103 };

	FireNet::Octets identity; // 帐号
	FireNet::Octets response; // 密码(使用平台sdk时发session)
	int logintype; // 登录类型(sdk类型) 1:locojoy platform sdk
	int gameid;
	FireNet::Octets gamekey;
	FireNet::Octets serverid;
	FireNet::Octets selfchannel; // 渠道号
	FireNet::Octets childchannel; // 子渠道号
	FireNet::Octets deviceid; // 设备唯一标识
	FireNet::Octets mid;
	int reserved1;
	FireNet::Octets reserved2;

	Response() {
		type = PROTOCOL_TYPE;
		logintype = 0;
		gameid = 0;
		reserved1 = 0;
	}

	Response(const FireNet::Octets & _identity_, const FireNet::Octets & _response_, int _logintype_, int _gameid_, const FireNet::Octets & _gamekey_, const FireNet::Octets & _serverid_, const FireNet::Octets & _selfchannel_, const FireNet::Octets & _childchannel_, const FireNet::Octets & _deviceid_, const FireNet::Octets & _mid_, int _reserved1_, const FireNet::Octets & _reserved2_)
		: identity(_identity_), response(_response_), logintype(_logintype_), gameid(_gameid_), gamekey(_gamekey_), serverid(_serverid_), selfchannel(_selfchannel_), childchannel(_childchannel_), deviceid(_deviceid_), mid(_mid_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit Response(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
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

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
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

	aio::Protocol * Clone() const { return new Response(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
