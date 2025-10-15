package rpcgen.java;

import java.util.*;
import java.io.*;
import rpcgen.types.*;

class ParamName extends TypeNameJava {

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

	public static void makeConstructWithParam(Bean bean, PrintStream ps, String prefix) {
		if (bean.getVariables().isEmpty())
			return;

		ps.println(prefix + "public " + bean.getName() + "(" + getParamList(bean.getVariables()) + ") {");
		for (Variable var : bean.getVariables())
			ps.println(prefix + "	this." + var.getName() + " = _" + var.getName() + "_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	
}
