	// Rpc

	enum { PROTOCOL_TYPE = 206 };

	GetMaxOnlineNum(void * // define stub
		, gnet::GetMaxOnlineNumArg * argument
		, gnet::GetMaxOnlineNumRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new GetMaxOnlineNum(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 64; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::GetMaxOnlineNumArg* getArgument() { return (gnet::GetMaxOnlineNumArg*)argument; }
	gnet::GetMaxOnlineNumRes* getResult() { return (gnet::GetMaxOnlineNumRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::GetMaxOnlineNumArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

