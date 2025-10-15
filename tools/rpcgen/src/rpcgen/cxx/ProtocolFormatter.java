package rpcgen.cxx;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import rpcgen.Main;
import rpcgen.Modules;
import rpcgen.Namespace;
import rpcgen.Naming;
import rpcgen.types.Bean;
import rpcgen.types.Variable;

class ProtocolFormatter {
	private rpcgen.Protocol protocol;

	public ProtocolFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * 生成协议文件，RpcFormatter会重载覆盖这个实现
	 * @param output
	 */
	void make(java.io.File output) {
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		final PrintStream ps = space.fopen(output, name + ".hpp", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("#pragma once");
		ps.println("");
		/*
		 * Aio支持协议模块化。但有时候跨模块引用协议能提供一些便利，虽然这违背了模块封装规则。
		 * 当然，对于能跨模块使用的协议必须不能包含实现，只能当作纯粹的数据结构，否则就说不清楚了。
		 * 对于Aio，生成协议的时候，不再直接包含"rpcgen.hpp"。原来在rpcgen.hpp中的常用头文件生成到
		 * 自己的 _depends_ 头文件中。由于协议是不覆盖的，这只会影响新定义的协议。
		 * see makeDepends and Rpcgen.printCommonInclude
		 */
		if (Main.isAIO)
			ps.println("//#include \"rpcgen.hpp\" // uncomment this line if need.");
		else
			ps.println("#include \"rpcgen.hpp\"");

		ps.println("#include \"rpcgen/" + space.join("/") + "/_depends_" + name + "_.hpp\"");
		ps.println("");
		space.begin(ps);
		ps.println("");
		ps.println("class " + name + " : public " + Rpcgen.ProtocolBaseClassName);
		ps.println("{");
		ps.println("public:");
		ps.println("	#include \"rpcgen/" + space.join("/") + "/" + name + ".inc\"");
		ps.println("");
		ps.println("	virtual void Process(Manager *, Manager::Session::ID ) " + processBody());
		ps.println("");
		ps.println("};");
		ps.println("");
		space.end(ps);

		ps.close();
	}

	/**
	 * 生成协议包含文件，RpcFormatter会重载覆盖这个实现
	 * @param output
	 */
	void makeInc(java.io.File output) {
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		Namespace space = protocol.getSpace();
		String name = protocol.getName();
		PrintStream ps = space.fopen(output, name + ".inc", true);

		// define
		ps.println("	// Protocol");
		ps.println("");
		ps.println("	enum { PROTOCOL_TYPE = " + protocol.getType() + " };");
		ps.println("");
		bf.declareEnums(ps);
		bf.declareVariables(ps);

		// default constructor
		ps.println("	" + protocol.getName() + "() {");
		ps.println("		type = PROTOCOL_TYPE;");

		for (Variable var : bean.getVariables())
			var.getType().accept(new Construct(ps, var, "		"));
		ps.println("	}");
		ps.println("");

		if (false == bean.getVariables().isEmpty()) {
			final String beanName =
				bean.getVariables().size() == 1 ? ("explicit " + bean.getName()) : bean.getName();
			ps.println("	" + beanName + "(" + ParamName.getParamList(bean.getVariables()) + ")");
			ps.println("		: " + ParamName.getInitList(bean.getVariables()) + " {");
			ps.println("		type = PROTOCOL_TYPE;");
			ps.println("	}");
			ps.println("");
		}

		ps.println("	explicit " + protocol.getName() + "(void *) : Protocol(PROTOCOL_TYPE) { } // define stub");
		ps.println("");
		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");

		if (Main.wOstream){
			WOstream.make(bean, ps, "	", protocol.getName());
		}
		
		if (Main.cxxTrace){
			Trace.make(bean, ps, "	", protocol.getName());
		}
	
		// protocolImplement
		ps.println("	" + Rpcgen.ProtocolBaseClassName + " * Clone() const { return new "
				+ protocol.getName() + "(*this); }");
		ps.println("	int  PriorPolicy( ) const { return " + protocol.getPrior() + "; }");
		ps.println("	bool SizePolicy(size_t size) const { return size <= "
				+ protocol.getMaxsize() + "; }");
		if (Main.isClient){
			int waiting = -1;
			if(!protocol.getWaitingProtocol().isEmpty())
				waiting = Integer.valueOf(protocol.getWaitingProtocol());
			if(waiting >= 0)
				ps.println("	int WaitingProtocol() const { return " + protocol.getWaitingProtocol() + "; }");
		}
		ps.close();
	}

	/**
	 * 是否生成空的处理方法。如果有模块映射定义则不生成（for 梦诛）。
	 * @return
	 */
	private String processBody() {
		rpcgen.Module module = Modules.getInstance().findProcessModule(protocol);
		if (null != module)
			return "; // see Module " + module.getName();

		return "{ /* protocol handle */ }";
	}

	/**
	 * 生成协议依赖头文件
	 * @param output
	 */
	void makeDepends(java.io.File output) {
		PrintStream ps = protocol.getSpace().fopen(output, "_depends_" + protocol.getName() + "_.hpp", true);

		ps.println("#pragma once");
		ps.println("");

		if (Main.isAIO)
			Rpcgen.printCommonInclude(ps);

		Set<Bean> depends = new HashSet<Bean>();
		protocol.dependBeans(depends);
		for (Bean bean : Naming.sort(depends)) {
			if (null == bean.getInclude())
				continue;
			ps.println("#include \"rpcgen/" + bean.getSpace().join("/") + "/" + bean.getName() + ".hpp\"");
		}
		ps.println("");

		ps.close();
	}

	/**
	 * 打印协议存根，RpcFormatter会重载这个实现。
	 * @param ps
	 */
	void printStub(PrintStream ps) {
		ps.println("static " + protocol.fullName("::") + " " + protocol.fullName("_") + "((void*)0);");
	}
}
