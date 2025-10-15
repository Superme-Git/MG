package rpcgen.js;

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
		name = type.fullName();
	}
	@Override
	public void visit(TypeLong type) {
		name = "io.Int64";
	}

	@Override
	public void visit(TypeOctets type) {
		name = "io.Octets";
	}
	
	
	@Override
	public void visit(TypeByte type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeInt type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeShort type)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public void visit(TypeFloat type) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void visit(TypeText type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeList type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeVector type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeSet type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(TypeMap type) {
		throw new UnsupportedOperationException();
	}
}
