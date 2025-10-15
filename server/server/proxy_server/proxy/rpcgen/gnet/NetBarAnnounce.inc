	// Protocol

	enum { PROTOCOL_TYPE = 529 };

	int userid;
	int aid;
	int zoneid;
	int title;
	GNET::Octets rolename;
	int netbarid;
	int netbarzone;
	GNET::Octets netbarname;
	int netbarlevel;
	GNET::Octets netbartitle;
	GNET::Octets awardtype;
	int reserved1;
	int reserved2;

	NetBarAnnounce() {
		type = PROTOCOL_TYPE;
		userid = 0;
		aid = 0;
		zoneid = 0;
		title = 0;
		netbarid = 0;
		netbarzone = 0;
		netbarlevel = 0;
		reserved1 = 0;
		reserved2 = 0;
	}

	NetBarAnnounce(int _userid_, int _aid_, int _zoneid_, int _title_, const GNET::Octets & _rolename_, int _netbarid_, int _netbarzone_, const GNET::Octets & _netbarname_, int _netbarlevel_, const GNET::Octets & _netbartitle_, const GNET::Octets & _awardtype_, int _reserved1_, int _reserved2_)
		: userid(_userid_), aid(_aid_), zoneid(_zoneid_), title(_title_), rolename(_rolename_), netbarid(_netbarid_), netbarzone(_netbarzone_), netbarname(_netbarname_), netbarlevel(_netbarlevel_), netbartitle(_netbartitle_), awardtype(_awardtype_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit NetBarAnnounce(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << aid;
		_os_ << zoneid;
		_os_ << title;
		_os_ << rolename;
		_os_ << netbarid;
		_os_ << netbarzone;
		_os_ << netbarname;
		_os_ << netbarlevel;
		_os_ << netbartitle;
		_os_ << awardtype;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> aid;
		_os_ >> zoneid;
		_os_ >> title;
		_os_ >> rolename;
		_os_ >> netbarid;
		_os_ >> netbarzone;
		_os_ >> netbarname;
		_os_ >> netbarlevel;
		_os_ >> netbartitle;
		_os_ >> awardtype;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new NetBarAnnounce(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
