package rpcgen.java;

import java.io.PrintStream;
import rpcgen.types.*;

class Equals implements Visitor {
	private Variable variable;
	private String another;
	private boolean isEquals;
	private String text;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public boolean equals(Object _o1_) {");
		ps.println(prefix + "	if (_o1_ == this) return true;");
		ps.println(prefix + "	if (_o1_ instanceof " + bean.getName() + ") {");
		if (!bean.getVariables().isEmpty())
			ps.println(prefix + "		" + bean.getName() + " _o_ = (" + bean.getName() + ")_o1_;");
		for (Variable var : bean.getVariables()) {
			Equals e = new Equals(var, "_o_", false);
			var.getType().accept(e);
			ps.println(prefix + "		if (" + e.getText() + ") return false;");
		}
		ps.println(prefix + "		return true;");
		ps.println(prefix + "	}");
		ps.println(prefix + "	return false;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public String getText() {
		return text;
	}

	Equals(Variable variable, String another, boolean isEquals) {
		this.variable = variable;
		this.another = another;
		this.isEquals = isEquals;
	}

	@Override
	public void visit(Bean type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeByte type) {
		text =  variable.getName() + (isEquals ? " == " : " != ") + another + "." + variable.getName();
	}

	@Override
	public void visit(TypeFloat type) {
		text =  variable.getName() + (isEquals ? " == " : " != ") + another + "." + variable.getName();
	}

	@Override
	public void visit(TypeInt type) {
		text =  variable.getName() + (isEquals ? " == " : " != ") + another + "." + variable.getName();
	}

	@Override
	public void visit(TypeShort type) {
		text =  variable.getName() + (isEquals ? " == " : " != ") + another + "." + variable.getName();
	}
	
	@Override
	public void visit(TypeList type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeLong type) {
		text =  variable.getName() + (isEquals ? " == " : " != ") + another + "." + variable.getName();
	}

	@Override
	public void visit(TypeMap type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeOctets type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeSet type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeText type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}

	@Override
	public void visit(TypeVector type) {
		text =  (isEquals ? "" : "!") + variable.getName() + ".equals(" + another + "." + variable.getName() + ")";
	}
}
