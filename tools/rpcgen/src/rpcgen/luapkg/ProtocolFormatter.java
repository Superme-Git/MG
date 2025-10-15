package rpcgen.luapkg;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Namespace;


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
		final PrintStream ps = space.fopen(output, name + ".pkg", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("");
		/*
		 * Aio支持协议模块化。但有时候跨模块引用协议能提供一些便利，虽然这违背了模块封装规则。
		 * 当然，对于能跨模块使用的协议必须不能包含实现，只能当作纯粹的数据结构，否则就说不清楚了。
		 * 对于Aio，生成协议的时候，不再直接包含"rpcgen.hpp"。原来在rpcgen.hpp中的常用头文件生成到
		 * 自己的 _depends_ 头文件中。由于协议是不覆盖的，这只会影响新定义的协议。
		 * see makeDepends and Rpcgen.printCommonInclude
		 */

		ps.println("");
		space.begin(ps);
		ps.println("");
		ps.println("class " + name + " : public " + Rpcgen.ProtocolBaseClassName);
		ps.println("{");
		ps.println("public:");

		makeInc(ps);
		
		
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
	void makeInc(PrintStream ps) {
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		
		
		
		// define
		ps.println("	// Protocol");
		ps.println("");
		ps.println("");
		bf.declareEnums(ps);
		bf.declareVariables(ps);

		// default constructor
		ps.println("	" + protocol.getName() + "() ;");
		ps.println("");

		if (false == bean.getVariables().isEmpty()) {
			final String beanName =
				bean.getVariables().size() == 1 ? ("explicit " + bean.getName()) : bean.getName();
			ps.println("	" + beanName + "(" + ParamName.getParamList(bean.getVariables()) + ");");
			ps.println("");
		}

		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");

		// protocolImplement
		ps.println("	" + Rpcgen.ProtocolBaseClassName + " * Clone();");
	}

}
