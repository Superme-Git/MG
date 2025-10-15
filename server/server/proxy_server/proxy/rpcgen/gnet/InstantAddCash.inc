	// Rpc

	enum { PROTOCOL_TYPE = 8015 };

	InstantAddCash(void * // define stub
		, gnet::InstantAddCashArg * argument
		, gnet::InstantAddCashRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new InstantAddCash(*this); }
	int  PriorPolicy( ) const { return 1; }
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

	gnet::InstantAddCashArg* getArgument() { return (gnet::InstantAddCashArg*)argument; }
	gnet::InstantAddCashRes* getResult() { return (gnet::InstantAddCashRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::InstantAddCashArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

