package rpcgen.js;

import java.io.PrintStream;

import rpcgen.types.Bean;
import rpcgen.types.TypeByte;
import rpcgen.types.TypeFloat;
import rpcgen.types.TypeInt;
import rpcgen.types.TypeList;
import rpcgen.types.TypeLong;
import rpcgen.types.TypeMap;
import rpcgen.types.TypeOctets;
import rpcgen.types.TypeSet;
import rpcgen.types.TypeShort;
import rpcgen.types.TypeText;
import rpcgen.types.TypeVector;
import rpcgen.types.Variable;
import rpcgen.types.Visitor;

class Construct implements Visitor {
	private PrintStream ps;
	private Variable variable;
	private String prefix;

	public static void make(Bean bean, PrintStream ps) {
		ps.println("function " + bean.getName() + "() {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Construct(ps, var, "	this."));
		ps.println("}");
		ps.println("");
	}

	public Construct(PrintStream ps, Variable variable, String prefix) {
		this.ps = ps;
		this.variable = variable;
		this.prefix = prefix;
	}

	@Override
	public void visit(Bean type) {
		String typeName = TypeName.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + variable.getName() + " = .0;");
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + variable.getName() + " = 0;");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + variable.getName() + " = 0;");
	}

	@Override
	public void visit(TypeShort type) {
		ps.println(prefix + variable.getName() + " = 0;");
	}

	@Override
	public void visit(TypeLong type) {
		String typeName = TypeName.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeOctets type) {
		String typeName = TypeName.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeText type) {
		String value = variable.getInitial();
		String varname = variable.getName();
		ps.println(prefix + varname + " = \"" + value + "\";");
	}

	@Override
	public void visit(TypeList type) {
		ps.println(prefix + variable.getName() + " = [];");
	}

	@Override
	public void visit(TypeVector type) {
		ps.println(prefix + variable.getName() + " = [];");
	}

	@Override
	public void visit(TypeSet type) {
		ps.println(prefix + variable.getName() + " = [];");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + variable.getName() + " = [];");
	}
}
