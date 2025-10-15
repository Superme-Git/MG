package rpcgen.cxx;

import rpcgen.types.TypeList;
import rpcgen.types.TypeMap;
import rpcgen.types.TypeSet;
import rpcgen.types.TypeVector;

class BoxingName extends TypeName {

	public static String getName(rpcgen.types.Type type) {
		BoxingName visitor = new BoxingName();
		type.accept(visitor);
		return visitor.getName();
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
