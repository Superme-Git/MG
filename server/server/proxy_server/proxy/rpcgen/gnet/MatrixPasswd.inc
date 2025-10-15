	// Rpc

	enum { PROTOCOL_TYPE = 550 };

	MatrixPasswd(void * // define stub
		, gnet::MatrixPasswdArg * argument
		, gnet::MatrixPasswdRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new MatrixPasswd(*this); }
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

	gnet::MatrixPasswdArg* getArgument() { return (gnet::MatrixPasswdArg*)argument; }
	gnet::MatrixPasswdRes* getResult() { return (gnet::MatrixPasswdRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const gnet::MatrixPasswdArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

