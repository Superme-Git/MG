package rpcgen.lua;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import rpcgen.Main;
import rpcgen.Namespace;
import rpcgen.Naming;
import rpcgen.lua.Marshal;
import rpcgen.lua.Unmarshal;
import rpcgen.types.Bean;

class ProtocolFormatter {
	private rpcgen.Protocol protocol;

	public ProtocolFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * ʺ³ʐ­өτ¼�Formatter»◘՘¸²¸ȕ⹶ʵЖ
	 * @param output
	 */
	void make(java.io.File output) {
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName().toLowerCase();
		final PrintStream ps = space.fopen(output, name + ".lua", Main.overwriteAll);
		if (null == ps) return;
		
		makeRequire(ps);
		//makeNamespace(ps);
		makeTableBegin(ps);
		makeTableNew(ps);
		makeEncodeFunc(ps);
		makeMarshal(ps);
		makeUnmarshal(ps);		
		makeTableEnd(ps);

		ps.close();
	}

	private void makeRequire(PrintStream ps)
	{
		ps.println("require \"utils.tableutil\"");
		Set<Bean> depends = new HashSet<Bean>();
		protocol.dependBeans(depends);
		for (Bean bean : Naming.sort(depends)) {
			if (null == bean.getInclude())
				continue;
			ps.println("require \"protodef.rpcgen." + bean.getSpace().join(".").toLowerCase()
					+ "." + bean.getName().toLowerCase() + "\"");
		}
	}
	private void makeNamespace(PrintStream ps)
	{
		ps.println("luaproto = luaproto or {}");
		final Namespace space = protocol.getSpace();
		space.maketable(ps);
		ps.println("");
	}
	private void makeTableBegin(PrintStream ps)
	{
		final Namespace space = protocol.getSpace();
		
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		if (!bean.getEnums().isEmpty()) {
			ps.println(protocol.getName() + " = {");
			bf.declareEnums(ps);
			ps.println("}");
		} else {
			ps.println(protocol.getName() + " = {}");
		}
		
		ps.println(protocol.getName() + ".__index = " + protocol.getName());
//		ps.println("luaproto." + space.join(".") + "." + protocol.getName()
//				+ " = " + protocol.getName());
		
		ps.println("");
		if(!protocol.getWaitingProtocol().isEmpty())
			ps.println(protocol.getName() + ".waiting = " + protocol.getWaitingProtocol());
		ps.println("");
		
		ps.println("");
		ps.println(protocol.getName() + ".PROTOCOL_TYPE = " + protocol.getType());
		ps.println("");

		ps.println("function " + protocol.getName() + ".Create()");
		ps.println("	print(\"enter " +protocol.getName() +" create\")");
		ps.println("	return " + protocol.getName() + ":new()");
		ps.println("end");
	}
	private void  makeTableNew(PrintStream ps)
	{
		ps.println("function " + protocol.getName() + ":new()");
		ps.println("	local self = {}");
		ps.println("	setmetatable(self, "+ protocol.getName() + ")");
		ps.println("	self.type = self.PROTOCOL_TYPE");
		
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		bf.declareVariables(ps);
		
		//Validator.make(bean, ps, "	");		
		
		ps.println("	return self");
		ps.println("end");
	}
	private void  makeEncodeFunc(PrintStream ps)
	{
		ps.println("function " + protocol.getName() + ":encode()");
		ps.println("	local os = FireNet.Marshal.OctetsStream:new()");
		ps.println("	os:compact_uint32(self.type)");
		ps.println("	local pos = self:marshal(nil)");
		ps.println("	os:marshal_octets(pos:getdata())");
		ps.println("	pos:delete()");
		ps.println("	return os");
		ps.println("end");
	}
	private void  makeMarshal(PrintStream ps)
	{
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		Marshal.make(bean, ps, "	");
	}
	private void  makeUnmarshal(PrintStream ps)	
	{
		final rpcgen.types.Bean bean = protocol.getImplementBean();	
		Unmarshal.make(bean, ps, "	");
	}
	private void  makeTableEnd(PrintStream ps)
	{
		ps.println("return " + protocol.getName());
	}
}
