	// Protocol

	enum { PROTOCOL_TYPE = 8904 };

	GNET::Octets platid; // 平台唯一标识
	GNET::Octets orderserialplat; // 平台用的订单号
	GNET::Octets orderserialgame; // 游戏内自己用的订单号

	QueryOrderRequest() {
		type = PROTOCOL_TYPE;
	}

	QueryOrderRequest(const GNET::Octets & _platid_, const GNET::Octets & _orderserialplat_, const GNET::Octets & _orderserialgame_)
		: platid(_platid_), orderserialplat(_orderserialplat_), orderserialgame(_orderserialgame_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryOrderRequest(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << platid;
		_os_ << orderserialplat;
		_os_ << orderserialgame;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> platid;
		_os_ >> orderserialplat;
		_os_ >> orderserialgame;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new QueryOrderRequest(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
