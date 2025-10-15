package rpcgen.js;

import java.io.PrintStream;

/**
 * js Bean 代码生成
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
	public void make(PrintStream ps ) {
		ps.println("");
		Construct.make(bean, ps);
		ps.println(bean.getName() + ".prototype = new io.Marshal();");
		ps.println("");
		
		declareEnums(ps);

		Marshal.make(bean, ps);
		Unmarshal.make(bean, ps);
		Trace.make(bean, ps);
		
	}

	void declareEnums(PrintStream ps) {
		for (rpcgen.types.Enum e : bean.getEnums())
			ps.println(bean.getName() + "." + e.getName() + " = " + e.getValue() + "; " + e.getComment());
		if (!bean.getEnums().isEmpty())
			ps.println("");
	}
}
