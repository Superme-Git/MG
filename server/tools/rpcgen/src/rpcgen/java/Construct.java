package rpcgen.java;

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

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public " + bean.getName() + "() {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Construct(ps, var, prefix + "	"));
		ps.println(prefix + "}");
		ps.println("");
	}

	public Construct(PrintStream ps, Variable variable, String prefix) {
		this.ps = ps;
		this.variable = variable;
		this.prefix = prefix;
	}

	private void initial() {
		String value = variable.getInitial();
		if (!value.isEmpty()) {
			String varname = variable.getName();
			ps.println(prefix + varname + " = " + value + ";");
		}
	}

	@Override
	public void visit(Bean type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeFloat type) {
		this.initial();
	}

	@Override
	public void visit(TypeByte type) {
		this.initial();
	}

	@Override
	public void visit(TypeInt type) {
		this.initial();
	}

	@Override
	public void visit(TypeShort type) {
		this.initial();
	}
	
	@Override
	public void visit(TypeLong type) {
		this.initial();
	}

	@Override
	public void visit(TypeOctets type) {
		String typeName = TypeNameJava.getName(type);
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
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeVector type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeSet type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeMap type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + variable.getName() + " = new " + typeName + "();");
	}
}
