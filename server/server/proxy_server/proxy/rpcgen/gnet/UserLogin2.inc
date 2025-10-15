	// Rpc

	enum { PROTOCOL_TYPE = 8067 };

	UserLogin2(void * // define stub
		, gnet::UserLogin2Arg * argument
		, gnet::UserLogin2Res * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new UserLogin2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::UserLogin2Arg* getArgument() { return (gnet::UserLogin2Arg*)argument; }
	gnet::UserLogin2Res* getResult() { return (gnet::UserLogin2Res*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::UserLogin2Arg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

