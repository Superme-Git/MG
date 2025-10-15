	// Protocol

	enum { PROTOCOL_TYPE = 509 };

	int userid;
	int list_type;
	std::vector<gnet::RoleForbid> forbid;

	QueryUserForbid_Re() {
		type = PROTOCOL_TYPE;
		userid = 0;
		list_type = 0;
	}

	QueryUserForbid_Re(int _userid_, int _list_type_, const std::vector<gnet::RoleForbid> & _forbid_)
		: userid(_userid_), list_type(_list_type_), forbid(_forbid_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryUserForbid_Re(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<gnet::RoleForbid>::const_iterator _i_ = forbid.begin(); _i_ != forbid.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << list_type;
		_os_ << GNET::MarshalContainer(forbid);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> list_type;
		_os_ >> GNET::MarshalContainer(forbid);
		return _os_;
	}

	GNET::Protocol * Clone() const { return new QueryUserForbid_Re(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 4096; }
