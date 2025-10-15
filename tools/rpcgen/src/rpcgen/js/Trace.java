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

class Trace implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps) {
		ps.println(bean.getName() + ".prototype.toString = function() {");
		ps.println("	var _sb_ = \"" + bean.fullName() + "(\";");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Trace(var.getName(), ps, "	"));
		ps.println("	return _sb_ + \")\";");
		ps.println("};");
		ps.println("");
	}

	public Trace(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	private void trace() {
		ps.println(prefix + "_sb_ += this." + varname + " + \",\";");
	}
	
	private void traceArray() {
		ps.println(prefix + "_sb_ += \"[\" + this." + varname + " + \"],\";");
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
		this.traceArray();
	}

	@Override
	public void visit(TypeLong type) {
		this.trace();
	}

	@Override
	public void visit(TypeMap type) {
		this.traceArray();
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_sb_ += \"B\" + this." + varname + ".size()" + " + \",\";");
	}

	@Override
	public void visit(TypeSet type) {
		this.traceArray();
	}

	@Override
	public void visit(TypeText type) {
		this.trace();
	}

	@Override
	public void visit(TypeVector type) {
		this.traceArray();
	}
}
