package rpcgen.luapkg;

import java.io.PrintStream;

import rpcgen.Main;
import rpcgen.Namespace;


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
		final Namespace space = protocol.getSpace();
		final String name = protocol.getName();
		final PrintStream ps = space.fopen(output, name + ".pkg", Main.overwriteAll);
		if (null == ps) return;

		ps.println("");
		ps.println("");
		/*
		 * Aio֧��Э��ģ�黯������ʱ���ģ������Э�����ṩһЩ��������Ȼ��Υ����ģ���װ����
		 * ��Ȼ�������ܿ�ģ��ʹ�õ�Э����벻�ܰ���ʵ�֣�ֻ�ܵ�����������ݽṹ�������˵������ˡ�
		 * ����Aio������Э���ʱ�򣬲���ֱ�Ӱ���"rpcgen.hpp"��ԭ����rpcgen.hpp�еĳ���ͷ�ļ����ɵ�
		 * �Լ��� _depends_ ͷ�ļ��С�����Э���ǲ����ǵģ���ֻ��Ӱ���¶����Э�顣
		 * see makeDepends and Rpcgen.printCommonInclude
		 */

		ps.println("");
		space.begin(ps);
		ps.println("");
		ps.println("class " + name + " : public " + Rpcgen.ProtocolBaseClassName);
		ps.println("{");
		ps.println("public:");

		makeInc(ps);
		
		
		ps.println("");
		ps.println("};");
		ps.println("");
		space.end(ps);

		ps.close();
	}

	/**
	 * ����Э������ļ���RpcFormatter�����ظ������ʵ��
	 * @param output
	 */
	void makeInc(PrintStream ps) {
		final rpcgen.types.Bean bean = protocol.getImplementBean();
		final BeanFormatter bf = new BeanFormatter(bean);
		
		
		
		// define
		ps.println("	// Protocol");
		ps.println("");
		ps.println("");
		bf.declareEnums(ps);
		bf.declareVariables(ps);

		// default constructor
		ps.println("	" + protocol.getName() + "() ;");
		ps.println("");

		if (false == bean.getVariables().isEmpty()) {
			final String beanName =
				bean.getVariables().size() == 1 ? ("explicit " + bean.getName()) : bean.getName();
			ps.println("	" + beanName + "(" + ParamName.getParamList(bean.getVariables()) + ");");
			ps.println("");
		}

		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");

		// protocolImplement
		ps.println("	" + Rpcgen.ProtocolBaseClassName + " * Clone();");
	}

}
