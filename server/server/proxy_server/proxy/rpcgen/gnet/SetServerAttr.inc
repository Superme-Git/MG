	// Rpc

	enum { PROTOCOL_TYPE = 204 };

	SetServerAttr(void * // define stub
		, gnet::SetServerAttrArg * argument
		, gnet::SetServerAttrRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new SetServerAttr(*this); }
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

	gnet::SetServerAttrArg* getArgument() { return (gnet::SetServerAttrArg*)argument; }
	gnet::SetServerAttrRes* getResult() { return (gnet::SetServerAttrRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::SetServerAttrArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

