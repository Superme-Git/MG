	// Protocol

	enum { PROTOCOL_TYPE = 556 };

	int zoneid;
	int userid;
	int rate;
	int msg;
	std::vector<gnet::GPair> data;

	AddictionControl() {
		type = PROTOCOL_TYPE;
		zoneid = 0;
		userid = 0;
		rate = 0;
		msg = 0;
	}

	AddictionControl(int _zoneid_, int _userid_, int _rate_, int _msg_, const std::vector<gnet::GPair> & _data_)
		: zoneid(_zoneid_), userid(_userid_), rate(_rate_), msg(_msg_), data(_data_) {
		type = PROTOCOL_TYPE;
	}

	explicit AddictionControl(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<gnet::GPair>::const_iterator _i_ = data.begin(); _i_ != data.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << userid;
		_os_ << rate;
		_os_ << msg;
		_os_ << FireNet::MarshalContainer(data);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> userid;
		_os_ >> rate;
		_os_ >> msg;
		_os_ >> FireNet::MarshalContainer(data);
		return _os_;
	}

	aio::Protocol * Clone() const { return new AddictionControl(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
