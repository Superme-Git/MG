package rpcgen.luapkg;

import java.io.PrintStream;

import rpcgen.Namespace;
import rpcgen.types.Enum;
import rpcgen.types.Variable;

class BeanFormatter {
	private rpcgen.types.Bean bean;

	BeanFormatter(rpcgen.types.Bean bean) {
		this.bean = bean;
	}

	void make(java.io.File output) {
		Namespace space = bean.getSpace();
		String name = bean.getName();
		
		
		PrintStream ps = space.fopen(output, name + ".pkg", true);	

		ps.println("");
		ps.println("");

		ps.println("");
		space.begin(ps);
		ps.println("");
		String baseClassName = Rpcgen.BeanIsRpcData ? "FireNet::Rpc::Data" : "FireNet::Marshal";
		ps.println("class " + bean.getName() + " : public " + baseClassName);
		ps.println("{");
		ps.println("public:");
		declareEnums(ps);
		declareVariables(ps);
		Construct.make(bean, ps, "	");

		if (false == bean.getVariables().isEmpty()) {
			ps.println("	" + bean.getName() + "("
					+ ParamName.getParamList(bean.getVariables()) + ");");
		}

		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");
		ps.println("};");
		ps.println("");
		space.end(ps);

		ps.close();
	}

	void declareEnums(PrintStream ps) {
		if (!bean.getEnums().isEmpty()) {
			ps.println("	enum {");
			for (Enum e : bean.getEnums())
				ps.println("		" + e.declare(","));
			ps.println("	};");
			ps.println("");
		}
	}

	void declareVariables(PrintStream ps) {
		if (!bean.getVariables().isEmpty()) {
			for (Variable var : bean.getVariables())
				ps.println("	" + var.declare(TypeName.getName(var.getType())));
			ps.println("");
		}
	}
}
