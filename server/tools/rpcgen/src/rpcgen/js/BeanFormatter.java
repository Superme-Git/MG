package rpcgen.js;

import java.io.PrintStream;

/**
 * js Bean ��������
 */
class BeanFormatter {
	private rpcgen.types.Bean bean;

	public BeanFormatter(rpcgen.types.Bean bean) {
		this.bean = bean;
	}

	/**
	 * ��ָ��Ŀ¼������ Bean ����������롣
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
