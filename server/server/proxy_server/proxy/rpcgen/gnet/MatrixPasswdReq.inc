	// Protocol

	enum { PROTOCOL_TYPE = 113 };

	gnet::MatrixPasswdArg info; // 里面包含有帐号
	int localsid;
	GNET::Octets response; // 密码

	MatrixPasswdReq() {
		type = PROTOCOL_TYPE;
		localsid = 0;
	}

	MatrixPasswdReq(const gnet::MatrixPasswdArg & _info_, int _localsid_, const GNET::Octets & _response_)
		: info(_info_), localsid(_localsid_), response(_response_) {
		type = PROTOCOL_TYPE;
	}

	explicit MatrixPasswdReq(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!info._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << info;
		_os_ << localsid;
		_os_ << response;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> info;
		_os_ >> localsid;
		_os_ >> response;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new MatrixPasswdReq(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
