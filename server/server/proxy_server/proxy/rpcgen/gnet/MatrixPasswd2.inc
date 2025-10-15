	// Rpc

	enum { PROTOCOL_TYPE = 8066 };

	MatrixPasswd2(void * // define stub
		, gnet::MatrixPasswd2Arg * argument
		, gnet::MatrixPasswd2Res * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new MatrixPasswd2(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1024; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	gnet::MatrixPasswd2Arg* getArgument() { return (gnet::MatrixPasswd2Arg*)argument; }
	gnet::MatrixPasswd2Res* getResult() { return (gnet::MatrixPasswd2Res*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::MatrixPasswd2Arg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

