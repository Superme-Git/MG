	// Rpc

	enum { PROTOCOL_TYPE = 8002 };

	QueryUserid2(void * // define stub
		, gnet::QueryUserid2Arg * argument
		, gnet::QueryUserid2Res * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new QueryUserid2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 256; }
	bool TimePolicy(int timeout) const { return timeout <= 10; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::QueryUserid2Arg* getArgument() { return (gnet::QueryUserid2Arg*)argument; }
	gnet::QueryUserid2Res* getResult() { return (gnet::QueryUserid2Res*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::QueryUserid2Arg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

