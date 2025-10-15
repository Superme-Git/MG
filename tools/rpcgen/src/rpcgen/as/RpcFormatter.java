package rpcgen.as;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Namespace;
import rpcgen.Rpc;
import rpcgen.types.Bean;

/**
 * Rpc ´úÂëÉú³É
 */
class RpcFormatter extends ProtocolFormatter {
	private Rpc rpc;

	RpcFormatter(Rpc rpc) {
		super(rpc);
		this.rpc = rpc;
	}

	@Override
	void make(java.io.File output) {
		final Namespace space = rpc.getSpace();
		final String name = rpc.getName();
		PrintStream ps = space.fopen(output, name + ".as", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("package " + space + " {");
		ps.println("");
		ps.println("	import com.locojoy.marshal.Bean;");
		ps.println("	import com.locojoy.io.ClientManager");
		ps.println("	import " + space + "._" + name + "_inc");
		ps.println("");
		ps.println("	public class " + name + " extends _" + name + "_inc {");
		//ps.println("		include \"" + space.join("/") + "/_" + name + "_inc_.as\"");
		ps.println("");

		final String argName = rpc.getArgumentBean().fullName();
		final String resName = rpc.getResultBean().fullName();
		ps.println("		protected override function server(argument : Bean, result : Bean, client : ClientManager) : void {");
		ps.println("			//_arg : " + argName + " = " + argName + "(argument);");
		ps.println("			//_res : " + resName + " = " + resName + "(result);");
		ps.println("			// server handle");
		ps.println("		}");
		ps.println("	");
		ps.println("		protected override function client(argument:Bean, result:Bean, client:ClientManager) : void {"	);
		ps.println("			//_arg : " + argName + " = " + argName + "(argument);");
		ps.println("			//_res : " + resName + " = " + resName + "(result);");
		ps.println("		}");
		ps.println("	");
		ps.println("		protected override function timeout(argument:Bean) : void {");
		ps.println("			//_arg : " + argName + " = " + argName + "(argument);");
		ps.println("		} ");
		ps.println("	}");
		ps.println("}");

		ps.close();
	}

	@Override
	void makeInc(java.io.File output) {
		final Bean bean = rpc.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		final Namespace space = rpc.getSpace();
		final String name = rpc.getName();
		final PrintStream ps = space.fopen(output, "_" + name + "_inc.as", true);

		// define
		ps.println("");
		ps.println("package " + space + " {");
		ps.println("");
		ps.println("	import com.locojoy.io.Rpc;");
		ps.println("	import com.locojoy.marshal.Bean;");
		ps.println("	import com.locojoy.io.ClientManager");
		ps.println("");
		ps.println("	public class _" + name + "_inc extends com.locojoy.io.Rpc {");
		ps.println("	// Rpc inc file : " + rpc.fullName());
		ps.println("");
		for (String impt : Import.imports(rpc)) ps.println("	" + impt);
		ps.println("");
		ps.println("	public static const PROTOCOL_TYPE : int = " + rpc.getType() + "; ");
		ps.println("");

		bf.declareEnums(ps);

		// default constructor
		ps.println("	public function _" + rpc.getName() + "_inc() {");
		ps.println("		ptype = PROTOCOL_TYPE;");
		ps.println("");
		ps.println("		arg = new " + rpc.getArgumentBean().fullName() + "();");
		ps.println("		arg = new " + rpc.getResultBean().fullName() + "();");
		ps.println("	}");
		ps.println("");

		// rpc Implement
		ps.println("	public override function getMaxSize() : uint {");
		ps.println("		return " +  rpc.getMaxsize() + ";");
		ps.println("	}");
		ps.println("	public override function getTimeout() : unit {");
		ps.println("		return " +  rpc.getTimeout() + ";");
		ps.println("	}");
		ps.println("	}");
		ps.println("}");
		
		ps.close();
	}
}
