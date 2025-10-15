	// Protocol

	enum { PROTOCOL_TYPE = 59 };

	int priority;
	GNET::Octets msg;
	GNET::Octets hostname;
	GNET::Octets servicename;

	StatInfoVital() {
		type = PROTOCOL_TYPE;
		priority = 0;
	}

	StatInfoVital(int _priority_, const GNET::Octets & _msg_, const GNET::Octets & _hostname_, const GNET::Octets & _servicename_)
		: priority(_priority_), msg(_msg_), hostname(_hostname_), servicename(_servicename_) {
		type = PROTOCOL_TYPE;
	}

	explicit StatInfoVital(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << priority;
		_os_ << msg;
		_os_ << hostname;
		_os_ << servicename;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> priority;
		_os_ >> msg;
		_os_ >> hostname;
		_os_ >> servicename;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new StatInfoVital(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
