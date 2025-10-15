	// Rpc

	enum { PROTOCOL_TYPE = 8063 };

	CouponExchange(void * // define stub
		, gnet::CouponExchangeArg * argument
		, gnet::CouponExchangeRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new CouponExchange(*this); }
	int  PriorPolicy( ) const { return 2; }
	bool SizePolicy(size_t size) const { return size <= 128; }
	bool TimePolicy(int timeout) const { return timeout <= 15; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::CouponExchangeArg* getArgument() { return (gnet::CouponExchangeArg*)argument; }
	gnet::CouponExchangeRes* getResult() { return (gnet::CouponExchangeRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::CouponExchangeArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

