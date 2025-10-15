	// Protocol

	enum { PROTOCOL_TYPE = 8905 };

	enum {
		restype_query = 0, // 查询返回结果
		restype_notify = 0, // 主动通告返回结果
	};

	int errorcode;
	int restype;
	GNET::Octets platid; // 平台唯一标识
	GNET::Octets orderserialplat; // 平台用的订单号
	GNET::Octets orderserialgame; // 游戏内自己用的订单号
	int userid; // 如果返回的结果中，并没有userid的信息，该值为-1
	GNET::Octets vars;

	QueryOrderResponse() {
		type = PROTOCOL_TYPE;
		errorcode = ErrorCodes::error_succeed;
		restype = restype_query;
		userid = -1;
	}

	QueryOrderResponse(int _errorcode_, int _restype_, const GNET::Octets & _platid_, const GNET::Octets & _orderserialplat_, const GNET::Octets & _orderserialgame_, int _userid_, const GNET::Octets & _vars_)
		: errorcode(_errorcode_), restype(_restype_), platid(_platid_), orderserialplat(_orderserialplat_), orderserialgame(_orderserialgame_), userid(_userid_), vars(_vars_) {
		type = PROTOCOL_TYPE;
	}

	explicit QueryOrderResponse(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << errorcode;
		_os_ << restype;
		_os_ << platid;
		_os_ << orderserialplat;
		_os_ << orderserialgame;
		_os_ << userid;
		_os_ << vars;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> errorcode;
		_os_ >> restype;
		_os_ >> platid;
		_os_ >> orderserialplat;
		_os_ >> orderserialgame;
		_os_ >> userid;
		_os_ >> vars;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new QueryOrderResponse(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
