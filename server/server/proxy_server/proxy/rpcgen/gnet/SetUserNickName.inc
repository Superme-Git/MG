	// Rpc

	enum { PROTOCOL_TYPE = 8004 };

	SetUserNickName(void * // define stub
		, gnet::SetUserNickNameArg * argument
		, gnet::SetUserNickNameRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new SetUserNickName(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
	bool TimePolicy(int timeout) const { return timeout <= 15; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::SetUserNickNameArg* getArgument() { return (gnet::SetUserNickNameArg*)argument; }
	gnet::SetUserNickNameRes* getResult() { return (gnet::SetUserNickNameRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::SetUserNickNameArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

