	// Rpc

	enum { PROTOCOL_TYPE = 15 };

	UserLogin(void * // define stub
		, gnet::UserLoginArg * argument
		, gnet::UserLoginRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new UserLogin(*this); }
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

	gnet::UserLoginArg* getArgument() { return (gnet::UserLoginArg*)argument; }
	gnet::UserLoginRes* getResult() { return (gnet::UserLoginRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::UserLoginArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

