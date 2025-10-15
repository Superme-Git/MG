package rpcgen.java;

import rpcgen.types.TypeByte;
import rpcgen.types.TypeFloat;
import rpcgen.types.TypeInt;
import rpcgen.types.TypeList;
import rpcgen.types.TypeLong;
import rpcgen.types.TypeMap;
import rpcgen.types.TypeSet;
import rpcgen.types.TypeShort;
import rpcgen.types.TypeVector;

class BoxingName extends TypeNameJava {

	public static String getName(rpcgen.types.Type type) {
		BoxingName visitor = new BoxingName();
		type.accept(visitor);
		return visitor.getName();
	}

	@Override
	public void visit(TypeByte type) {
		name = "Byte";
	}

	@Override
	public void visit(TypeShort type) {
		name = "Short";
	}

	@Override
	public void visit(TypeInt type) {
		name = "Integer";
	}

	@Override
	public void visit(TypeLong type) {
		name = "Long";
	}

	@Override
	public void visit(TypeFloat type) {
		name = "Float";
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
