package rpcgen.java;

import java.io.PrintStream;

import rpcgen.types.Bean;
import rpcgen.types.Type;
import rpcgen.types.TypeByte;
import rpcgen.types.TypeCollection;
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

class CopyJava implements Visitor {
	private PrintStream ps;
	private String varname;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		String classname = bean.getName();
		ps.println(prefix + "public " + classname + "(" + classname + " _o_) {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new CopyJava(ps, prefix + "	", var));
		ps.println(prefix + "}");
		ps.println("");
	}

	public CopyJava(PrintStream ps, String prefix, Variable variable) {
		this.ps = ps;
		this.prefix = prefix;
		this.varname = variable.getName();
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}

	@Override
	public void visit(TypeShort type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}
	
	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}

	@Override
	public void visit(TypeOctets type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + varname + " = new " + typeName + "(_o_." + varname + ");");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + varname + " = _o_." + varname + ";");
	}

	private void copyCollection(TypeCollection type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + varname + " = new " + typeName + "();");

		Type valuetype = type.getValuetype();
		if (valuetype.isConstType()) {
			ps.println(prefix + varname + ".addAll(_o_." + varname + ");");
			return;
		}
		String boxingName = TypeNameJava.getName(valuetype);
		ps.println(prefix + "for (" + boxingName + " _v_ : _o_." + varname + ")");
		ps.println(prefix + "	" + varname + ".add(new " + boxingName + "(_v_));");
	}

	@Override
	public void visit(TypeList type) {
		this.copyCollection(type);
	}

	@Override
	public void visit(TypeVector type) {
		this.copyCollection(type);
	}

	@Override
	public void visit(TypeSet type) {
		this.copyCollection(type);
	}

	@Override
	public void visit(TypeMap type) {
		String typeName = TypeNameJava.getName(type);
		Type keytype = type.getKeytype();
		Type valuetype = type.getValuetype();
		ps.println(prefix + varname + " = new " + typeName + "();");

		String key = BoxingName.getName(keytype);
		String value = BoxingName.getName(valuetype);
		String KV = "<" + key + ", " + value + ">";

		String keycopy = keytype.isConstType() ? "_e_.getKey()" : ("new " + key + "(_e_.getKey())");
		String valuecopy = valuetype.isConstType() ? "_e_.getValue()" : ("new " + value + "(_e_.getValue())");
		ps.println(prefix + "for (java.util.Map.Entry" + KV + " _e_ : _o_." + varname + ".entrySet())");
		ps.println(prefix + "	" + varname + ".put(" + keycopy + ", " + valuecopy + ");");
	}

	@Override
	public void visit(Bean type) {
		String typeName = TypeNameJava.getName(type);
		ps.println(prefix + varname + " = new " + typeName + "(_o_." + varname + ");");
	}
}
