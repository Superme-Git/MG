package rpcgen.as;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Namespace;
import rpcgen.Protocol;
import rpcgen.types.Variable;
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
	 * 生成协议文件，RpcFormatter会重载覆盖这个实现
	 * @param output
	 */
	void make(java.io.File output) {
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		PrintStream ps = space.fopen(output, name + ".as", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("package " + space + " {");
		ps.println("");
		ps.println("	import com.locojoy.io.ClientManager");
		ps.println("	import " + space + "._" + name + "_inc");
		ps.println("");
		ps.println("	public class " + name + " extends _" + name + "_inc {");
		//ps.println("		include \"" + space.join("/") + "/_" + name + "_inc_.as\"");
		ps.println("");
		ps.println("		public override function process(client : ClientManager) : void { ");
		ps.println("			// protocol handle");
		ps.println("		} ");
		ps.println("	");
		ps.println("	}");
		ps.println("}");

		ps.close();
	}

	/**
	 * 生成协议包含文件，RpcFormatter会重载覆盖这个实现
	 * @param output
	 */
	void makeInc(java.io.File output) {
		final Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		final PrintStream ps = space.fopen(output, "_" + name + "_inc.as", true);

		// define
		ps.println("");
		ps.println("package " + space + " {");
		ps.println("");
		ps.println("	import com.locojoy.io.Protocol");
		ps.println("	import com.locojoy.io.ClientManager");
		ps.println("");
		ps.println("	public class _" + name + "_inc extends com.locojoy.io.Protocol {");
		ps.println("	// Protocol inc file : " + protocol.fullName());
		ps.println("");
		ps.println("	import com.locojoy.marshal.Marshal;");
		ps.println("	import com.locojoy.marshal.Stream;");
		ps.println("");
		for (String impt : Import.imports(protocol)) ps.println("	" + impt);
		ps.println("");
		ps.println("	public static const PROTOCOL_TYPE : int = " + protocol.getType() + "; ");
		ps.println("");

		bf.declareEnums(ps);
		bf.declareVariables(ps);

		// default constructor
		ps.println("	public function _" + protocol.getName() + "_inc() {");
		ps.println("		ptype = PROTOCOL_TYPE;");
		ps.println("");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Construct(ps, var, "		"));
		ps.println("	}");
		ps.println("");

		//Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		//if (bean.isComparable())
		//	CompareTo.make(bean, ps, "	");

		// protocolImplement
		ps.println("	public override function getMaxSize() : uint {");
		ps.println("		return " +  protocol.getMaxsize() + ";");
		ps.println("	}");
		ps.println("	");
		ps.println("	}");
		ps.println("}");

		ps.close();
	}

}
