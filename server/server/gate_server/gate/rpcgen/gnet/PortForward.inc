	// Protocol

	enum { PROTOCOL_TYPE = 109 };

	int command; // 命令码。
	int portsid; // 映射编号，由发起方(eConnect)分配。
	int code; // 命令参数，具体含义由相应命令决定。
	GNET::Octets data; // 命令参数，具体含义由相应命令决定。

	PortForward() {
		type = PROTOCOL_TYPE;
		command = 0;
		portsid = 0;
		code = 0;
	}

	PortForward(int _command_, int _portsid_, int _code_, const GNET::Octets & _data_)
		: command(_command_), portsid(_portsid_), code(_code_), data(_data_) {
		type = PROTOCOL_TYPE;
	}

	explicit PortForward(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << command;
		_os_ << portsid;
		_os_ << code;
		_os_ << data;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> command;
		_os_ >> portsid;
		_os_ >> code;
		_os_ >> data;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new PortForward(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65536; }
