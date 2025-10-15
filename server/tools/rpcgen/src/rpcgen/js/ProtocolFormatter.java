package rpcgen.js;

import java.io.PrintStream;
import rpcgen.Protocol;
import rpcgen.types.Bean;

/**
 * Protocol 代码生成
 */
class ProtocolFormatter {
	private Protocol protocol;

	public ProtocolFormatter(Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * 生成协议文件
	 * @param output
	 */
	void make(PrintStream ps) {
		final Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		final String name = protocol.getName();
		
		ps.println("");
		Construct.make(bean, ps);
		
		ps.println(name + ".prototype = new io.Protocol();");
		ps.println("");
		
		ps.println(name + ".PROTOCOL_TYPE = "  + protocol.getType() + "; ");
		ps.println("");
		
		bf.declareEnums(ps);
		Marshal.make(bean, ps);
		Unmarshal.make(bean, ps);
		Trace.make(bean, ps);
		
		ps.println(name + ".prototype.getType = function(){");
		ps.println("	return " + protocol.getType() + ";");
		ps.println("};");
		ps.println("");
		
		ps.println(name + ".prototype.getMaxSize = function(){");
		ps.println("	return " +  protocol.getMaxsize() + ";");
		ps.println("};");
		ps.println("");
	}

}
