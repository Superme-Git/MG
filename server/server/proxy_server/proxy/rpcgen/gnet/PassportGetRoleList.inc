	// Rpc

	enum { PROTOCOL_TYPE = 8013 };

	PassportGetRoleList(void * // define stub
		, gnet::PassportGetRoleListArg * argument
		, gnet::PassportGetRoleListRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new PassportGetRoleList(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
	bool TimePolicy(int timeout) const { return timeout <= 15; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::PassportGetRoleListArg* getArgument() { return (gnet::PassportGetRoleListArg*)argument; }
	gnet::PassportGetRoleListRes* getResult() { return (gnet::PassportGetRoleListRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::PassportGetRoleListArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

