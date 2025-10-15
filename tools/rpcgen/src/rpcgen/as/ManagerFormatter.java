package rpcgen.as;

import java.io.PrintStream;

import rpcgen.Main;

class ManagerFormatter {
	private rpcgen.Manager manager;

	ManagerFormatter(rpcgen.Manager manager) {
		this.manager = manager;
	}

	void make(java.io.File output) {
		final String name = manager.getName();
		final PrintStream ps = manager.getSpace().fopen(output, name + ".as", Main.overwriteAll);
		if (null == ps)
			return; // file should be exist;

		ps.println("package " + manager.getSpace() + " {");
		ps.println("");
		ps.println("	import flash.system.Security;");
		ps.println("");
		ps.println("	import com.locojoy.io.ClientManager;");
		ps.println("");
		ps.println("	public class " + name + " extends ClientManager {");
		ps.println("");
		ps.println("		public function " + name + "(remoteHost:String, remotePort:int) {");
		ps.println("			super(remoteHost, remotePort);");
		ps.println("			// put your code here.");
		//ps.println("			//Security.loadPolicyFile("http://172.16.32.32:8080/flashtest/crossdomain.xml");");
		ps.println("		}");
		ps.println("");
		ps.println("		public override function onConnect() {");
		ps.println("			// put your code here.");
		ps.println("		}");
		ps.println("");
		ps.println("		public override function onClose() {");
		ps.println("			//put your code here.");
		ps.println("		}");
		ps.println("");
		ps.println("		public override function onAbort() {");
		ps.println("			//put your code here.");
		ps.println("		}");
		ps.println("");
//		ps.println("		public function getState():int {");
//		ps.println("			return state;");
//		ps.println("		}");
//		ps.println("");
//		ps.println("		public function setState(s:int) {");
//		ps.println("			state = s;");
//		ps.println("		}");
//		ps.println("");
//		ps.println("		private var sprite:ASIOTest;");
//		ps.println("		private var state:int;");
//		ps.println("	}");
		ps.println("");
		ps.println("}");

		ps.close();
	}
}
