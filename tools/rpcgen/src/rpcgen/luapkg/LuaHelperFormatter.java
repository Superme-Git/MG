package rpcgen.luapkg;

import java.io.PrintStream;
import rpcgen.Namespace;


class LuaHelperFormatter {
	private rpcgen.Protocol protocol;

	public LuaHelperFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * ����tolua����
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
	 * ����FireClient.Protocol����ӳ���
	 * @param output
	 */
	void makeTable(PrintStream ps) {		
		final String name = protocol.getName();
		ps.println("FireClient.Protocol.to" + name + " = FireClient.to" + name);
	}

}
