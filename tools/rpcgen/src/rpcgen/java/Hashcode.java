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

class Hashcode implements Visitor {

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public int hashCode() {");
		ps.println(prefix + "	int _h_ = 0;");
		for (Variable var : bean.getVariables()) {
			Hashcode e = new Hashcode(var.getName());
			var.getType().accept(e);
			ps.println(prefix + "	_h_ += " + e.getText() + ";");
		}
		ps.println(prefix + "	return _h_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	private String varname;
	private String text;

	public String getText() {
		return text;
	}

	Hashcode(String varname) {
		this.varname = varname;
	}
	@Override
	public void visit(Bean type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeByte type) {
		text = "(int)" + varname;
	}

	@Override
	public void visit(TypeInt type) {
		text = varname;
	}

	@Override
	public void visit(TypeShort type) {
		text = varname;
	}
	
	@Override
	public void visit(TypeLong type) {
		text = "(int)" + varname;
	}

	@Override
	public void visit(TypeOctets type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeText type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeList type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeVector type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeSet type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeMap type) {
		text = varname + ".hashCode()";
	}

	@Override
	public void visit(TypeFloat type) {
		text = "Float.floatToIntBits(" + varname + ")";
	}

}
