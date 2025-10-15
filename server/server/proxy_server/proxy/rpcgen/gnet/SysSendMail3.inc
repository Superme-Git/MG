	// Protocol

	enum { PROTOCOL_TYPE = 8068 };

	int64_t orderid;
	int userid;
	int64_t roleid;
	GNET::Octets rolename;
	GNET::Octets mail_title;
	GNET::Octets mail_context;
	int attach_money;
	gnet::MailGoodsInventory attach_goods;

	SysSendMail3() {
		type = PROTOCOL_TYPE;
		orderid = 0;
		userid = 0;
		roleid = 0;
		attach_money = 0;
	}

	SysSendMail3(int64_t _orderid_, int _userid_, int64_t _roleid_, const GNET::Octets & _rolename_, const GNET::Octets & _mail_title_, const GNET::Octets & _mail_context_, int _attach_money_, const gnet::MailGoodsInventory & _attach_goods_)
		: orderid(_orderid_), userid(_userid_), roleid(_roleid_), rolename(_rolename_), mail_title(_mail_title_), mail_context(_mail_context_), attach_money(_attach_money_), attach_goods(_attach_goods_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail3(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!attach_goods._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << orderid;
		_os_ << userid;
		_os_ << roleid;
		_os_ << rolename;
		_os_ << mail_title;
		_os_ << mail_context;
		_os_ << attach_money;
		_os_ << attach_goods;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> orderid;
		_os_ >> userid;
		_os_ >> roleid;
		_os_ >> rolename;
		_os_ >> mail_title;
		_os_ >> mail_context;
		_os_ >> attach_money;
		_os_ >> attach_goods;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail3(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 4096; }
