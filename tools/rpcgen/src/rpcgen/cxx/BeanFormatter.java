package rpcgen.cxx;

import java.io.PrintStream;

import rpcgen.Main;
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
		PrintStream ps = space.fopen(output, name + ".hpp", true);

		ps.println("");
		ps.println("#pragma once");
		ps.println("");
		for (String inc : Include.includes(bean))
			ps.println("" + inc);
		ps.println("");
		space.begin(ps);
		ps.println("");
		String baseClassName = "";
		if (Main.isClient){		
			baseClassName = Rpcgen.BeanIsRpcData ? "FireNet::Rpc::Data" : "FireNet::Marshal";
		}else{
			baseClassName = Rpcgen.BeanIsRpcData ? "GNET::Rpc::Data" : "GNET::Marshal";
		}
		ps.println("class " + bean.getName() + " : public " + baseClassName);
		ps.println("{");
		ps.println("public:");
		declareEnums(ps);
		declareVariables(ps);
		Construct.make(bean, ps, "	");

		if (false == bean.getVariables().isEmpty()) {
			ps.println("	" + bean.getName() + "("
					+ ParamName.getParamList(bean.getVariables()) + ")");
			ps.println("		: "
					+ ParamName.getInitList(bean.getVariables()) + " {");
			ps.println("	}");
			ps.println("");
		}

		Validator.make(bean, ps, "	");
		Marshal.make(bean, ps, "	");
		Unmarshal.make(bean, ps, "	");
		if (Rpcgen.BeanIsRpcData)
			rpcdataImplement(ps);
		if (bean.isComparable())
			CompareTo.make(bean, ps, "	");
		if (Main.wOstream){
			WOstream.make(bean, ps, "	", null);
		}
		if (Main.cxxTrace)
			Trace.make(bean, ps, "	", null);
		
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

	private void rpcdataImplement(PrintStream ps) {
		if (Main.isClient){		
			ps.println("	virtual FireNet::Rpc::Data * Clone() const ");
		}else{
			ps.println("	virtual GNET::Rpc::Data * Clone() const ");
		}
		ps.println("	{");
		ps.println("		return new " + bean.getName() + "(*this);");
		ps.println("	}");
		ps.println("");
		if (Main.isClient){		
			ps.println("	virtual FireNet::Rpc::Data & operator=(const FireNet::Rpc::Data & d)");
		}else{
			ps.println("	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)");
		}
		ps.println("	{");
		ps.println("		if (&d == this) return *this;");
		ps.println("		if (const " + bean.getName() + " *o = dynamic_cast<const "
				+ bean.getName() + "*>(&d)) return *this = *o;");
		ps.println("		return *this;");
		ps.println("	}");
		ps.println("");
		ps.println("	" + bean.getName() + "& operator=(const " + bean.getName() + " & d)");
		ps.println("	{");
		ps.println("		if (&d == this) return *this;");
		for (Variable var : bean.getVariables())
			ps.println("		" + var.getName() + " = d." + var.getName() + ";");
		ps.println("		return *this;");
		ps.println("	}");
		ps.println("");
	}
}
