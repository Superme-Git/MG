	// Protocol

	enum { PROTOCOL_TYPE = 4216 };

	int tid;
	int sysid;
	char sys_type;
	int64_t receiver;
	GNET::Octets title;
	GNET::Octets context;
	gnet::GRoleInventory attach_obj;
	int attach_money;

	SysSendMail2() {
		type = PROTOCOL_TYPE;
		tid = 0;
		sysid = 0;
		sys_type = 0;
		receiver = 0;
		attach_money = 0;
	}

	SysSendMail2(int _tid_, int _sysid_, char _sys_type_, int64_t _receiver_, const GNET::Octets & _title_, const GNET::Octets & _context_, const gnet::GRoleInventory & _attach_obj_, int _attach_money_)
		: tid(_tid_), sysid(_sysid_), sys_type(_sys_type_), receiver(_receiver_), title(_title_), context(_context_), attach_obj(_attach_obj_), attach_money(_attach_money_) {
		type = PROTOCOL_TYPE;
	}

	explicit SysSendMail2(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		if (!attach_obj._validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << tid;
		_os_ << sysid;
		_os_ << sys_type;
		_os_ << receiver;
		_os_ << title;
		_os_ << context;
		_os_ << attach_obj;
		_os_ << attach_money;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> tid;
		_os_ >> sysid;
		_os_ >> sys_type;
		_os_ >> receiver;
		_os_ >> title;
		_os_ >> context;
		_os_ >> attach_obj;
		_os_ >> attach_money;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new SysSendMail2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 4096; }
