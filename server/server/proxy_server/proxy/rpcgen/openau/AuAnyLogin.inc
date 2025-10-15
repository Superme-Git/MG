	// Rpc

	enum { PROTOCOL_TYPE = 8903 };

	AuAnyLogin(void * // define stub
		, openau::AuAnyLoginArg * argument
		, openau::AuAnyLoginRes * result
		) : Rpc(PROTOCOL_TYPE, argument, result) { }

	GNET::Protocol * Clone() const { return new AuAnyLogin(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 1048576; }
	bool TimePolicy(int timeout) const { return timeout <= 20; }

	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnServer(manager, sid);
	}

	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)
	{
		OnClient(manager, sid);
	}

	openau::AuAnyLoginArg* getArgument() { return (openau::AuAnyLoginArg*)argument; }
	openau::AuAnyLoginRes* getResult() { return (openau::AuAnyLoginRes*)result; }

	bool static Send(Manager * manager, Manager::Session::ID sid, const openau::AuAnyLoginArg & arg)
	{
		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);
		return manager->Send(sid, rpc);
	}

