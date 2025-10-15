package rpcgen.types;

import java.util.*;

import rpcgen.Namespace;

public class TypeList extends TypeCollection {
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean isComparable() {
		return false;
	}

	public Type compile(Namespace space, String key, String value) {
		return new TypeList(space, key, value);
	}

	private TypeList(Namespace space, String key, String value) {
		_compile(space, key, value);
	}

	TypeList(Map<String, Type> types) {
		types.put(getName(), this);
	}

	@Override
	public String getName() {
		return "list";
	}
}
