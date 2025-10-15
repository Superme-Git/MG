package rpcgen.luapkg;

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

class CompareTo implements Visitor {
	private Variable variable;
	private String another;
	private String text;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "bool operator<(const " + bean.getName() + " &_o_) const ;");
		ps.println("");
	}

	public String getText() {
		return text;
	}

	CompareTo(Variable variable, String another) {
		this.variable = variable;
		this.another = another;
	}

	public void visit(Bean type) {
		if (!type.isComparable())
			throw new UnsupportedOperationException("compareTo with " + type);
		text = variable.getName() + ".compareTo(" + another + "." + variable.getName() + ")";
	}

	public void visit(TypeByte type) {
		text = variable.getName() + " - " + another + "." + variable.getName();
	}

	public void visit(TypeInt type) {
		text = variable.getName() + " - " + another + "." + variable.getName();
	}

	public void visit(TypeShort type)
	{
		text = variable.getName() + " - " + another + "." + variable.getName();		
	}

	public void visit(TypeLong type) {
		text = "FireNet::signum(" + variable.getName() + " - " + another + "." + variable.getName() + ")";
	}

	public void visit(TypeFloat type) {
		//text = "Float.compare(" + variable.getName() + ", " + another + "." + variable.getName() + ")";
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeText type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeOctets type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeList type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeVector type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeSet type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}

	public void visit(TypeMap type) {
		throw new UnsupportedOperationException("compareTo with " + type);
	}
}
