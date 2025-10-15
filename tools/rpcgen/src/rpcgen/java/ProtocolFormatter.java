package rpcgen.java;

import java.io.PrintStream;

/**
 * protocol java ��������
 *
 */
class ProtocolFormatter {
	private rpcgen.Protocol protocol;

	public ProtocolFormatter(rpcgen.Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * ����Э���ļ���RpcFormatter�����ظ������ʵ��
	 * @param output
	 */
	void make(java.io.File output) {
		rpcgen.Parser rp = new rpcgen.Parser();
		// Э���ļ��Ѿ�������������滻���ɵĴ���
		if (protocol.getSpace().fparse(output, protocol.getName() + ".java", rp)) {
			rp.verify(protocol.fullName());
			PrintStream ps = protocol.getSpace().fopen(output, protocol.getName() + ".java", true);
			rp.printBeforeImport(ps);
			printImport(ps);
			rp.printImportToDefine(ps);
			printDefine(ps);
			rp.printAfterDefine(ps);
			ps.close();
			return;
		}

		// ��һ������Э���ļ������������Ĵ����ļ�
		PrintStream ps = protocol.getSpace().fopen(output, protocol.getName() + ".java", false);

		ps.println("");
		ps.println("package " + protocol.getSpace() + ";");
		ps.println("");
		printImport(ps);
		ps.println("");
		ps.println("public class " + protocol.getName() + " extends __" + protocol.getName() + "__ {");
		ps.println("	@Override");
		ps.println("	protected void process() {");
		ps.println("		// protocol handle");
		ps.println("	}");
		ps.println("");
		printDefine(ps);
		ps.println("");
		ps.println("}");
		ps.println("");

		ps.close();
	}

	private void printImport(PrintStream ps) {
		ps.println("" + rpcgen.Parser.IMPORT_BEGIN);
		ps.println("// {{{ DO NOT EDIT THIS");
		ps.println("import com.locojoy.base.Marshal.OctetsStream;");
		ps.println("import com.locojoy.base.Marshal.MarshalException;");
		ps.println("");
		ps.println("abstract class __" + protocol.getName() + "__ extends mkio.Protocol { }");
		ps.println("");
		protocol.getImplementBean().printComment(ps);
		ps.println("// DO NOT EDIT THIS }}}");
		ps.println("" + rpcgen.Parser.IMPORT_END);
	}

	private void printDefine(PrintStream ps) {
		ps.println("	" + rpcgen.Parser.DEFINE_BEGIN);
		ps.println("	// {{{ DO NOT EDIT THIS");
		ps.println("	public static final int PROTOCOL_TYPE = " + protocol.getType() + ";");
		ps.println("");
		ps.println("	public int getType() {");
		ps.println("		return " + protocol.getType() + ";");
		ps.println("	}");
		ps.println("");
		new rpcgen.java.BeanFormatter(protocol.getImplementBean()).printDefine(ps);
		ps.println("	// DO NOT EDIT THIS }}}");
		ps.println("	" + rpcgen.Parser.DEFINE_END);
	}

}
