	// Rpc

	enum { PROTOCOL_TYPE = 8009 };

	GetAddCashSN2(void * // define stub
		, gnet::GetAddCashSN2Arg * argument
		, gnet::GetAddCashSN2Res * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new GetAddCashSN2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 128; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::GetAddCashSN2Arg* getArgument() { return (gnet::GetAddCashSN2Arg*)argument; }
	gnet::GetAddCashSN2Res* getResult() { return (gnet::GetAddCashSN2Res*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::GetAddCashSN2Arg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

