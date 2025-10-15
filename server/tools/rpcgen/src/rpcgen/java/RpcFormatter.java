package rpcgen.java;

import java.io.PrintStream;

import rpcgen.Parser;

/**
 * rpc java 代码生成
 */
class RpcFormatter extends ProtocolFormatter {
	private rpcgen.Rpc rpc;

	public RpcFormatter(rpcgen.Rpc rpc) {
		super(rpc);
		this.rpc = rpc;
	}

	@Override
	void make(java.io.File output) {
		Parser rp = new Parser();
		if (rpc.getSpace().fparse(output, rpc.getName() + ".java", rp)) {
			rp.verify(rpc.fullName());
			PrintStream ps = rpc.getSpace().fopen(output, rpc.getName() + ".java", true);
			rp.printBeforeImport(ps);
			printImport(ps);
			rp.printImportToDefine(ps);
			printDefine(ps);
			rp.printAfterDefine(ps);
			ps.close();
			return;
		}

		PrintStream ps = rpc.getSpace().fopen(output, rpc.getName() + ".java", false);

		ps.println("");
		ps.println("package " + rpc.getSpace() + ";");
		ps.println("");
		printImport(ps);
		ps.println("");
		ps.println("public class " + rpc.getName() + " extends __" + rpc.getName() + "__ {");
		ps.println("	@Override");
		ps.println("	protected void onServer() {");
		ps.println("		// request handle");
		ps.println("	}");
		ps.println("");
		ps.println("	@Override");
		ps.println("	protected void onClient() {");
		ps.println("		// response handle");
		ps.println("	}");
		ps.println("");
		ps.println("	@Override");
		ps.println("	protected void onTimeout(int code) {");
		ps.println("		// client only. 当使用 submit 方式调用 rpc 时，不会产生这个回调。");
		ps.println("	}");
		ps.println("");
		printDefine(ps);
		ps.println("}");
		ps.println("");

		ps.close();
	}

	private void printImport(PrintStream ps) {
		ps.println("" + Parser.IMPORT_BEGIN);
		ps.println("// {{{ DO NOT EDIT THIS");
		ps.println("");
		ps.println("abstract class __" + rpc.getName() + "__ extends mkio.Rpc<"
				+ rpc.getArgumentBean().fullName() + ", " + rpc.getResultBean().fullName()
				+ "> { }");
		rpc.getImplementBean().printComment(ps);
		ps.println("// DO NOT EDIT THIS }}}");
		ps.println("" + Parser.IMPORT_END);
	}

	private void printDefine(PrintStream ps) {
		ps.println("	" + Parser.DEFINE_BEGIN);
		ps.println("	// {{{ DO NOT EDIT THIS");
		ps.println("	public int getType() {");
		ps.println("		return " + rpc.getType() + ";");
		ps.println("	}");
		ps.println("");
		ps.println("	public " + rpc.getName() + "() {");
		ps.println("		super.setArgument(new " + rpc.getArgumentBean().fullName() + "());");
		ps.println("		super.setResult(new " + rpc.getResultBean().fullName() + "());");
		ps.println("	}");
		ps.println("");
		ps.println("	public " + rpc.getName() + "(" + rpc.getArgumentBean().fullName() + " argument) {");
		ps.println("		super.setArgument(argument);");
		ps.println("		super.setResult(new " + rpc.getResultBean().fullName() + "());");
		ps.println("	}");
		ps.println("");
		ps.println("	public int getTimeout() {");
		ps.println("		return 1000 * " + rpc.getTimeout() + ";");
		ps.println("	}");
		ps.println("");
		ps.println("	// DO NOT EDIT THIS }}}");
		ps.println("	" + Parser.DEFINE_END);
	}

}
