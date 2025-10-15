	// Protocol

	enum { PROTOCOL_TYPE = 544 };

	int userid;
	std::vector<int> auth;

	QueryUserPrivilege3_Re() {
		type = PROTOCOL_TYPE;
		userid = 0;
	}

	QueryUserPrivilege3_Re(int _userid_, const std::vector<int> & _auth_)
		: userid(_userid_), auth(_auth_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryUserPrivilege3_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << GNET::MarshalContainer(auth);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> GNET::MarshalContainer(auth);
		return _os_;
	}

	GNET::Protocol * Clone() const { return new QueryUserPrivilege3_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 4096; }
