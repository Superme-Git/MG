	// Rpc

	enum { PROTOCOL_TYPE = 205 };

	SetMaxOnlineNum(void * // define stub
		, gnet::SetMaxOnlineNumArg * argument
		, gnet::SetMaxOnlineNumRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new SetMaxOnlineNum(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::SetMaxOnlineNumArg* getArgument() { return (gnet::SetMaxOnlineNumArg*)argument; }
	gnet::SetMaxOnlineNumRes* getResult() { return (gnet::SetMaxOnlineNumRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::SetMaxOnlineNumArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

