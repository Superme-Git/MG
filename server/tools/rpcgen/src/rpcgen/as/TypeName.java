package rpcgen.as;

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

class TypeName implements Visitor {
	protected String name; // subclass BoxingName

	public String getName() {
		return name;
	}

	public static String getName(rpcgen.types.Type type) {
		TypeName visitor = new TypeName();
		type.accept(visitor);
		return visitor.getName();
	}

	@Override
	public void visit(Bean type) {
		name = (type.getSpace() + "." + type.getName());
	}

	@Override
	public void visit(TypeByte type) {
		name = "int";
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
		name = "com.locojoy.types.Int64";
	}

	@Override
	public void visit(TypeFloat type) {
		name = "Number";
	}

	@Override
	public void visit(TypeOctets type) {
		name = "com.locojoy.types.Octets";
	}

	@Override
	public void visit(TypeText type) {
		throw new UnsupportedOperationException("string! as"); //name = "String";
	}

	@Override
	public void visit(TypeList type) {
		String valuename = TypeName.getName(type.getValuetype());
		name = "Vector.<" + valuename + ">";
	}

	@Override
	public void visit(TypeVector type) {
		String valuename = TypeName.getName(type.getValuetype());
		name = "Vector.<" + valuename + ">";
	}

	@Override
	public void visit(TypeSet type) {
		String valuename = TypeName.getName(type.getValuetype());
		name = "Vector.<" + valuename + ">";
	}

	@Override
	public void visit(TypeMap type) {
		name = "Vector.<Object>";
	}
}
