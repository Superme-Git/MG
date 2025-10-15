package rpcgen.luapkg;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Namespace;

class RpcFormatter extends ProtocolFormatter {
	private rpcgen.Rpc rpc;

	RpcFormatter(rpcgen.Rpc rpc) {
		super(rpc);
		this.rpc = rpc;
	}

	@Override
	void make(java.io.File output) {
		Namespace space = rpc.getSpace();
		String name = rpc.getName();
		PrintStream ps = space.fopen(output, name + ".pkg", Main.overwriteAll);
		if (null == ps) return;
		
		ps.println("");


		ps.println("");
		space.begin(ps);
		ps.println("");
		ps.println("class " + name + " : public FireNet::Rpc");
		ps.println("{");
		ps.println("public:");
		
		makeInc(ps);
		
		ps.println("");


		ps.println("};");
		ps.println("");
		space.end(ps);

		ps.close();
	}

	@Override
	void makeInc(PrintStream ps) {
		final rpcgen.types.Bean bean = rpc.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);


		ps.println("	// Rpc");
		ps.println("");
		ps.println("	enum { PROTOCOL_TYPE = " + rpc.getType() + " };");
		ps.println("");
		bf.declareEnums(ps);
		ps.println("	" + rpc.getName() + "(void * // define stub");
		ps.println("		, " + rpc.getArgumentBean().fullName("::") + " * argument");
		ps.println("		, " + rpc.getResultBean().fullName("::") + " * result");
		ps.println("		) : Rpc(PROTOCOL_TYPE, argument, result) { }");
		ps.println("");

		ps.println("	" + Rpcgen.ProtocolBaseClassName + " * Clone() const { return new " + rpc.getName() + "(*this); }");
		ps.println("	int  PriorPolicy( ) const { return " + rpc.getPrior() + "; }");
		ps.println("	bool SizePolicy(size_t size) const { return size <= " + rpc.getMaxsize() + "; }");

		ps.println("	bool TimePolicy(int timeout) const { return timeout <= " + rpc.getTimeout() + "; }");
		ps.println("");
		ps.println("	void Server(FireNet::Rpc::Data *, FireNet::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		ps.println("	{");
		ps.println("		OnServer(manager, sid);");
		ps.println("	}");
		ps.println("");
		ps.println("	void Client(FireNet::Rpc::Data *, FireNet::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		ps.println("	{");
		ps.println("		OnClient(manager, sid);");
		ps.println("	}");
		ps.println("");
		ps.println("	" + rpc.getArgumentBean().fullName("::") + "* getArgument() { return ("
				+ rpc.getArgumentBean().fullName("::") + "*)argument; }");
		ps.println("	" + rpc.getResultBean().fullName("::") + "* getResult() { return ("
				+ rpc.getResultBean().fullName("::") + "*)result; }");
		ps.println("");
		ps.println("	bool static Send(Manager * manager, Manager::Session::ID sid, const "
				+ rpc.getArgumentBean().fullName("::") + " & arg)");
		ps.println("	{");
		ps.println("		Rpc * rpc = FireNet::Rpc::Call(PROTOCOL_TYPE, &arg);");
		ps.println("		return manager->Send(sid, rpc);");
		ps.println("	}");
		ps.println("");

	}
}
