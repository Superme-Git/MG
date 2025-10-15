package rpcgen.types;

import java.util.*;

import rpcgen.Namespace;

public abstract class TypeCollection extends Type {
	private Type valuetype;

	@Override
	public void depends(Set<Type> incls) {
		if (incls.add(this))
			valuetype.depends(incls);
	}

	public Type getValuetype() {
		return valuetype;
	}

	void _compile(Namespace space, String key, String value) {
		if (key != null && !key.isEmpty())
			throw new RuntimeException(getName() + " type does not need a key. " + key);

		valuetype = Type.compile(space, value, null, null);
	}
}
