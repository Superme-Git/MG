package rpcgen.js;

import java.io.PrintStream;

import rpcgen.types.Bean;
import rpcgen.types.Type;
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
import rpcgen.types.Visitor;

/**
 * 定义局部变量
 */
class Define implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public Define(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "var " + varname + ";");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "var " + varname + ";");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "var " + varname + ";");
	}

	@Override
	public void visit(TypeLong type) {
		this.define(type);
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "var " + varname + ";");
	}

	@Override
	public void visit(TypeOctets type) {
		this.define(type);
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "var " + varname + ";");
	}

	private void define(Type type) {
		String typeName = TypeName.getName(type);
		ps.println(prefix + "var " + varname + " = new " + typeName + "();");
	}

	@Override
	public void visit(TypeList type) {
		ps.println(prefix + "var " + varname + " = [];");
	}

	@Override
	public void visit(TypeVector type) {
		ps.println(prefix + "var " + varname + " = [];");
	}

	@Override
	public void visit(TypeSet type) {
		ps.println(prefix + "var " + varname + " = [];");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + "var " + varname + " = [];");
	}

	@Override
	public void visit(Bean type) {
		this.define(type);
	}
}
