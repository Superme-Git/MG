package rpcgen.java;

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
class DefineJava implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public DefineJava(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	@Override
	public void visit(TypeByte type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	@Override
	public void visit(TypeInt type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	@Override
	public void visit(TypeShort type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}
	
	@Override
	public void visit(TypeLong type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	@Override
	public void visit(TypeFloat type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	@Override
	public void visit(TypeOctets type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	@Override
	public void visit(TypeText type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + typeName + " " + varname + ";");
	}

	private void define(Type type) {
		String tName = TypeNameJava.getName(type);
		ps.println(prefix + tName + " " + varname + " = new " + tName + "();");
	}

	@Override
	public void visit(TypeList type) {
		this.define(type);
	}

	@Override
	public void visit(TypeVector type) {
		this.define(type);
	}

	@Override
	public void visit(TypeSet type) {
		this.define(type);
	}

	@Override
	public void visit(TypeMap type) {
		this.define(type);
	}

	@Override
	public void visit(Bean type) {
		this.define(type);
	}
}
