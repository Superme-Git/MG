	// Protocol

	enum { PROTOCOL_TYPE = 211 };

	enum {
		ALGORITHM_NONE = 0x0, // 仅密码
		ALGORITHM_CARD = 0x00010000, // 密保卡
		ALGORITHM_HANDSET = 0x00020000, // 手机密保
		ALGORITHM_USBKEY = 0x00030000, // usbkey
		ALGORITHM_PHONE = 0x00040100, // 电话密保
		ALGORITHM_USBKEY2 = 0x00050000, // usbkey2
	};

	int userid;
	int retcode;
	int func; // 新手卡字段
	int funcparm; // 新手卡附属字段
	int loginip; // 客户端登录ip
	char blisgm; // 是否为GM
	std::vector<int> auth; // GM权限列表，具体权限见相关文档
	int algorithm; // 账号安全级别
	char gender; // 0-female,1-male,2-unknown
	GNET::Octets nickname; // 昵称

	UserInfoRep() {
		type = PROTOCOL_TYPE;
		userid = 0;
		retcode = 0;
		func = 0;
		funcparm = 0;
		loginip = 0;
		blisgm = 0;
		algorithm = 0;
		gender = 0;
	}

	UserInfoRep(int _userid_, int _retcode_, int _func_, int _funcparm_, int _loginip_, char _blisgm_, const std::vector<int> & _auth_, int _algorithm_, char _gender_, const GNET::Octets & _nickname_)
		: userid(_userid_), retcode(_retcode_), func(_func_), funcparm(_funcparm_), loginip(_loginip_), blisgm(_blisgm_), auth(_auth_), algorithm(_algorithm_), gender(_gender_), nickname(_nickname_) {
		type = PROTOCOL_TYPE;
	}

	explicit UserInfoRep(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << retcode;
		_os_ << func;
		_os_ << funcparm;
		_os_ << loginip;
		_os_ << blisgm;
		_os_ << GNET::MarshalContainer(auth);
		_os_ << algorithm;
		_os_ << gender;
		_os_ << nickname;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> retcode;
		_os_ >> func;
		_os_ >> funcparm;
		_os_ >> loginip;
		_os_ >> blisgm;
		_os_ >> GNET::MarshalContainer(auth);
		_os_ >> algorithm;
		_os_ >> gender;
		_os_ >> nickname;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new UserInfoRep(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
