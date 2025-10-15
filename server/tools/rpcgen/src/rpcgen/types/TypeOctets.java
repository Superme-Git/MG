package rpcgen.types;

import java.util.*;

import rpcgen.Namespace;

public class TypeOctets extends Type {
	@Override
	public Type compile(Namespace space, String key, String value) {
		if (key != null && key.length() > 0)
			throw new RuntimeException(getName() + " type does not need a key. " + key);

		if (value != null && value.length() > 0)
			throw new RuntimeException(getName() + " type does not need a value. " + value);

		return this;
	}

	@Override
	public boolean isComparable() {
		return false;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void depends(Set<Type> incls) {
		incls.add(this);
	}

	@Override
	public String getName() {
		return "octets";
	}

	TypeOctets(Map<String, Type> types) {
		types.put(getName(), this);
	}
}
