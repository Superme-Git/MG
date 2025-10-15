package rpcgen.java;

import java.io.PrintStream;

/**
 * java Bean 代码生成
 */
class BeanFormatter {
	private rpcgen.types.Bean bean;

	public BeanFormatter(rpcgen.types.Bean bean) {
		this.bean = bean;
	}

	/**
	 * 在指定目录下生成 Bean 的完整类代码。
	 * @param output
	 * @param bean
	 */
	public void make(java.io.File output) {
		PrintStream ps = bean.getSpace().fopen(output, bean.getName() + ".java", true);

		ps.println("");
		ps.println("package " + bean.getSpace() + ";");
		ps.println("");
		printImport(ps);
		String c = bean.isComparable() ? (", Comparable<" + bean.getName() + ">") : "";
		ps.println("public class " + bean.getName() + " implements Marshal " + c + "{");
		printDefine(ps);
		ps.println("}");
		ps.println("");

		ps.close();
	}

	private void printImport(PrintStream ps) {
		ps.println("import com.locojoy.base.Marshal.Marshal;");
		ps.println("import com.locojoy.base.Marshal.OctetsStream;");
		ps.println("import com.locojoy.base.Marshal.MarshalException;");
		ps.println("");
		bean.printComment(ps);
	}

	public void printDefine(PrintStream ps) {
		// declare enums
		for (rpcgen.types.Enum e : bean.getEnums())
			ps.println("	public final static int " + e.declare(";"));
		if (!bean.getEnums().isEmpty())
			ps.println("");

		// declare variables
		for (rpcgen.types.Variable var : bean.getVariables())
			ps.println("	public " + var.declare(TypeNameJava.getName(var.getType())));
		ps.println("");

		Construct.make(bean, ps, "	");
		ParamName.makeConstructWithParam(bean, ps, "	");
		//CopyJava.make(this, ps, "	");
		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		Equals.make(bean, ps, "	");
		Hashcode.make(bean, ps, "	");
		Trace.make(bean, ps, "	");
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");
	}

}
