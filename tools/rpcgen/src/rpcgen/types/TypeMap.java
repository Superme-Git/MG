package rpcgen.types;

import java.util.*;

import rpcgen.Namespace;

public class TypeMap extends Type {
	private Type keytype;
	private Type valuetype;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean isComparable() {
		return false;
	}

	public Type compile(Namespace space, String key, String value) {
		return new TypeMap(space, key, value);
	}

	@Override
	public String getName() {
		return "map";
	}

	public Type getKeytype() {
		return keytype;
	}

	public Type getValuetype() {
		return valuetype;
	}

	@Override
	public void depends(Set<Type> incls) {
		if (incls.add(this)) {
			keytype.depends(incls);
			valuetype.depends(incls);
		}
	}

	private TypeMap(Namespace space, String key, String value) {
		if (key.length() == 0)
			throw new RuntimeException("map type need key");
		if (value.length() == 0)
			throw new RuntimeException("map type need value");

		keytype = Type.compile(space, key, null, null);
		valuetype = Type.compile(space, value, null, null);
	}

	TypeMap(Map<String, Type> types) {
		types.put(getName(), this);
	}
}
