package rpcgen.luapkg;

import java.util.*;
import rpcgen.types.*;

class ParamName implements Visitor {
	private String name;

	public static String getName(rpcgen.types.Type type) {
		ParamName visitor = new ParamName();
		type.accept(visitor);
		return visitor.name;
	}

	public static String getParamList(Collection<Variable> variables) {
		StringBuilder params = new StringBuilder();
		boolean first = true;
		for (Variable var : variables) {
			if (first) first = false; else params.append(", ");
			params.append(ParamName.getName(var.getType()))
				.append(" _").append(var.getName()).append("_");
		}
		return params.toString();
	}

	public static String getInitList(Collection<Variable> variables) {
		StringBuilder inits = new StringBuilder();
		boolean first = true;
		for (Variable var : variables) {
			if (first) first = false; else inits.append(", ");
			inits.append(var.getName()).append("(_").append(var.getName()).append("_)");
			first = false;
		}
		return inits.toString();
	}

	public void visit(Bean type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeByte type) {
		name = TypeName.getName(type);
	}

	public void visit(TypeInt type) {
		name = TypeName.getName(type);
	}

	public void visit(TypeShort type)
	{
		name = TypeName.getName(type);		
	}

	public void visit(TypeLong type) {
		name = TypeName.getName(type);
	}

	public void visit(TypeOctets type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeText type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeList type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeVector type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeSet type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeMap type) {
		name = "const " + TypeName.getName(type) + " &";
	}

	public void visit(TypeFloat type) {
		name = TypeName.getName(type);
	}
}
