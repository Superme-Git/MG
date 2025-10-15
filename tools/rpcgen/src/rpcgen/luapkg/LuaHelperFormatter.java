package rpcgen.luapkg;

import java.io.PrintStream;
import rpcgen.Namespace;


class LuaHelperFormatter {
	private rpcgen.Protocol protocol;

	public LuaHelperFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * 生成tolua函数
	 * @param output
	 */
	void makeFunc(PrintStream ps) {
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		
		ps.println("");
		ps.println("function FireClient.to" + name + "(p)");
		ps.println("	return tolua.cast(p, \"" + space.join("::") + "::" + name + "\" )");
		ps.println("end");
		ps.println("");
	}

	/**
	 * 生成FireClient.Protocol函数映射表
	 * @param output
	 */
	void makeTable(PrintStream ps) {		
		final String name = protocol.getName();
		ps.println("FireClient.Protocol.to" + name + " = FireClient.to" + name);
	}

}
