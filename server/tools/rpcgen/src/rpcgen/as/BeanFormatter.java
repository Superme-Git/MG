package rpcgen.as;

import java.io.PrintStream;

/**
 * as Bean 代码生成
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
		PrintStream ps = bean.getSpace().fopen(output, bean.getName() + ".as", true);

		ps.println("");
		ps.println("package " + bean.getSpace() + " {");
		ps.println("");
		ps.println("import com.locojoy.marshal.Bean;");
		ps.println("import com.locojoy.marshal.Marshal;");
		ps.println("import com.locojoy.marshal.Stream;");
		ps.println("");
		for (String impt : Import.imports(bean)) ps.println(impt);
		ps.println("");
		String c = ""; // bean.isComparable() ? (", Comparable<" + bean.getName() + ">") : "";
		ps.println("public class " + bean.getName() + " extends com.locojoy.marshal.Bean " + c + "{");

		declareEnums(ps);
		declareVariables(ps);

		Construct.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		/*
		ParamNameJava.makeConstructWithParam(this, ps, "	");
		//CopyJava.make(this, ps, "	");
		ValidatorJava.make(this, ps, "	");
		EqualsJava.make(this, ps, "	");
		HashcodeJava.make(this, ps, "	");
		TraceJava.make(this, ps, "	");
		if (this.isComparable())
			CompareToJava.make(this, ps, "	");
		*/

		ps.println("}");
		ps.println("}");

		ps.close();
	}

	void declareEnums(PrintStream ps) {
		// declare enums
		for (rpcgen.types.Enum e : bean.getEnums())
			ps.println("	public static const " + e.getName() + " : int = " + e.getValue() + "; " + e.getComment());
		if (!bean.getEnums().isEmpty())
			ps.println("");

	}

	void declareVariables(PrintStream ps) {
		// declare variables
		for (rpcgen.types.Variable v : bean.getVariables())
			ps.println("	public var "
					+ v.getName() + " : " + TypeName.getName(v.getType()) + "; " + v.getComment());
		ps.println("");
	}
}
