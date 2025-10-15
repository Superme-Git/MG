package rpcgen.cxx;

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
		PrintStream ps = space.fopen(output, name + ".hpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("#pragma once");
		ps.println("");
		ps.println("#include \"rpcgen.hpp\"");
		ps.println("#include \"rpcgen/" + space.join("/") + "/_depends_" + name + "_.hpp\"");
		ps.println("");
		space.begin(ps);
		ps.println("");
		if (Main.isClient){
			ps.println("class " + name + " : public FireNet::Rpc");
		}else{
			ps.println("class " + name + " : public GNET::Rpc");
		}
		ps.println("{");
		ps.println("public:");
		ps.println("	#include \"rpcgen/" + rpc.getSpace().join("/") + "/" + rpc.getName() + ".inc\"");
		ps.println("");
		ps.println("	void OnServer(Manager *, Manager::Session::ID )");
		ps.println("	{");
		ps.println("		//" + rpc.getArgumentBean().fullName("::") + "* arg = getArgument();");
		ps.println("		//" + rpc.getResultBean().fullName("::") + "* res = getResult();");
		ps.println("	}");
		ps.println("");
		ps.println("	void OnClient(Manager *, Manager::Session::ID )");
		ps.println("	{");
		ps.println("		//" + rpc.getArgumentBean().fullName("::") + "* arg = getArgument();");
		ps.println("		//" + rpc.getResultBean().fullName("::") + "* res = getResult();");
		ps.println("	}");
		ps.println("");
		ps.println("	void OnTimeout()");
		ps.println("	{");
		ps.println("		// client only");
		ps.println("		//" + rpc.getArgumentBean().fullName("::") + "* arg = getArgument();");
		ps.println("	}");
		ps.println("");
		ps.println("};");
		ps.println("");
		space.end(ps);

		ps.close();
	}

	@Override
	void makeInc(java.io.File output) {
		final rpcgen.types.Bean bean = rpc.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);

		Namespace space = rpc.getSpace();
		String name = rpc.getName();
		PrintStream ps = space.fopen(output, name + ".inc", true);

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
		if (Main.isClient){
			ps.println("	void Server(FireNet::Rpc::Data *, FireNet::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		}else{
			ps.println("	void Server(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		}
		ps.println("	{");
		ps.println("		OnServer(manager, sid);");
		ps.println("	}");
		ps.println("");
		if (Main.isClient){
			ps.println("	void Client(FireNet::Rpc::Data *, FireNet::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		}else{
			ps.println("	void Client(GNET::Rpc::Data *, GNET::Rpc::Data *, Manager * manager, Manager::Session::ID sid)");
		}
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
		if (Main.isClient){
			ps.println("		Rpc * rpc = FireNet::Rpc::Call(PROTOCOL_TYPE, &arg);");
		}else{
			ps.println("		Rpc * rpc = GNET::Rpc::Call(PROTOCOL_TYPE, &arg);");
		}
		ps.println("		return manager->Send(sid, rpc);");
		ps.println("	}");
		ps.println("");

		ps.close();
	}

	@Override
	void printStub(PrintStream ps) {
		ps.println("static " + rpc.fullName("::") + " " + rpc.fullName("_")
				+ "((void*)0, new " + rpc.getArgumentBean().fullName("::") + "(), new "
				+ rpc.getResultBean().fullName("::") + "());");
	}
}
