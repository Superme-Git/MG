	// Protocol

	enum { PROTOCOL_TYPE = 232 };

	int dtype;
	GNET::Octets content;

	BroadcastData() {
		type = PROTOCOL_TYPE;
		dtype = 0;
	}

	BroadcastData(int _dtype_, const GNET::Octets & _content_)
		: dtype(_dtype_), content(_content_) {
		type = PROTOCOL_TYPE;
	}

	explicit BroadcastData(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << dtype;
		_os_ << content;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> dtype;
		_os_ >> content;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new BroadcastData(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
