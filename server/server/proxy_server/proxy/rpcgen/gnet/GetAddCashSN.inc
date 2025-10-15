	// Rpc

	enum { PROTOCOL_TYPE = 514 };

	GetAddCashSN(void * // define stub
		, gnet::GetAddCashSNArg * argument
		, gnet::GetAddCashSNRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new GetAddCashSN(*this); }
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

	gnet::GetAddCashSNArg* getArgument() { return (gnet::GetAddCashSNArg*)argument; }
	gnet::GetAddCashSNRes* getResult() { return (gnet::GetAddCashSNRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::GetAddCashSNArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

