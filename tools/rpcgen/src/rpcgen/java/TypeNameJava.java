package rpcgen.java;

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
import rpcgen.types.Visitor;

class TypeNameJava implements Visitor {
	protected String name; // subclass BoxingName

	public String getName() {
		return name;
	}

	public static String getName(rpcgen.types.Type type) {
		TypeNameJava visitor = new TypeNameJava();
		type.accept(visitor);
		return visitor.getName();
	}

	@Override
	public void visit(Bean type) {
		name = (type.getSpace() + "." + type.getName());
	}

	@Override
	public void visit(TypeByte type) {
		name = "byte";
	}

	@Override
	public void visit(TypeInt type) {
		name = "int";
	}

	@Override
	public void visit(TypeShort type)
	{
		name = "short";		
	}

	@Override
	public void visit(TypeLong type) {
		name = "long";
	}

	@Override
	public void visit(TypeFloat type) {
		name = "float";
	}

	@Override
	public void visit(TypeOctets type) {
		name = "com.locojoy.base.Octets";
	}

	@Override
	public void visit(TypeText type) {
		name = "java.lang.String";
	}

	@Override
	public void visit(TypeList type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "java.util.LinkedList<" + valuename + ">";
	}

	@Override
	public void visit(TypeVector type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "java.util.ArrayList<" + valuename + ">";
	}

	@Override
	public void visit(TypeSet type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "java.util.HashSet<" + valuename + ">";
	}

	@Override
	public void visit(TypeMap type) {
		String key = BoxingName.getName(type.getKeytype());
		String value = BoxingName.getName(type.getValuetype());
		name = "java.util.HashMap<" + key + "," + value + ">";
	}
}
