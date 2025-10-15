	// Protocol

	enum { PROTOCOL_TYPE = 8075 };

	int paytype;
	int64_t orderid;
	int userid;
	int64_t roleid;
	GNET::Octets rolename;
	GNET::Octets mail_title;
	GNET::Octets mail_context;
	std::vector<gnet::FunctionInfo> functions;
	int attach_money;
	int reserved1;
	GNET::Octets reserved2;

	SysSendMail4() {
		type = PROTOCOL_TYPE;
		paytype = 0;
		orderid = 0;
		userid = 0;
		roleid = 0;
		attach_money = 0;
		reserved1 = 0;
	}

	SysSendMail4(int _paytype_, int64_t _orderid_, int _userid_, int64_t _roleid_, const GNET::Octets & _rolename_, const GNET::Octets & _mail_title_, const GNET::Octets & _mail_context_, const std::vector<gnet::FunctionInfo> & _functions_, int _attach_money_, int _reserved1_, const GNET::Octets & _reserved2_)
		: paytype(_paytype_), orderid(_orderid_), userid(_userid_), roleid(_roleid_), rolename(_rolename_), mail_title(_mail_title_), mail_context(_mail_context_), functions(_functions_), attach_money(_attach_money_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail4(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<gnet::FunctionInfo>::const_iterator _i_ = functions.begin(); _i_ != functions.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << paytype;
		_os_ << orderid;
		_os_ << userid;
		_os_ << roleid;
		_os_ << rolename;
		_os_ << mail_title;
		_os_ << mail_context;
		_os_ << GNET::MarshalContainer(functions);
		_os_ << attach_money;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> paytype;
		_os_ >> orderid;
		_os_ >> userid;
		_os_ >> roleid;
		_os_ >> rolename;
		_os_ >> mail_title;
		_os_ >> mail_context;
		_os_ >> GNET::MarshalContainer(functions);
		_os_ >> attach_money;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail4(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 4096; }
