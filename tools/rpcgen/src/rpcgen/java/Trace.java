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

class Trace implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public String toString() {");
		ps.println(prefix + "	StringBuilder _sb_ = new StringBuilder();");
		ps.println(prefix + "	_sb_.append(\"(\");");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Trace(var.getName(), ps, prefix + "	"));

		ps.println(prefix + "	_sb_.append(\")\");");
		ps.println(prefix + "	return _sb_.toString();");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Trace(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	private void trace() {
		ps.println(prefix + "_sb_.append(" + varname + ").append(\",\");");
	}

	@Override
	public void visit(Bean bean) {
		this.trace();
	}

	@Override
	public void visit(TypeByte type) {
		this.trace();
	}

	@Override
	public void visit(TypeFloat type) {
		this.trace();
	}

	@Override
	public void visit(TypeInt type) {
		this.trace();
	}

	@Override
	public void visit(TypeShort type)
	{
		this.trace();		
	}

	@Override
	public void visit(TypeList type) {
		this.trace();
	}

	@Override
	public void visit(TypeLong type) {
		this.trace();
	}

	@Override
	public void visit(TypeMap type) {
		this.trace();
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_sb_.append(\"B\").append(" + varname + ".size()).append(\",\");");
	}

	@Override
	public void visit(TypeSet type) {
		this.trace();
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_sb_.append(\"T\").append(" + varname + ".length()).append(\",\");");
	}

	@Override
	public void visit(TypeVector type) {
		this.trace();
	}
}
