package rpcgen.types;

import java.util.*;
import rpcgen.Namespace;

public abstract class Type extends rpcgen.Naming {
	public abstract Type compile(Namespace space, String key, String value);
	public abstract void depends(Set<Type> incls);
	public abstract void accept(Visitor visitor);

	public boolean isConstType() {
		return false;
	}

	public abstract boolean isComparable();

	protected Type(String name) {
		super(name);
	}

	// »ù±¾Àà
	protected Type() {
		super(null);
	}

	@Override
	public String toString() {
		return "rpcgen." + getName();
	}

	/////////////////////////////////////////////////////////////////////////////
	private static Map<String, Type> types = new HashMap<String, Type>();

	public static void add(Namespace names, Type type) {
		String fullname = names.toString() + "." + type.getName();

		if (types.put(fullname, type) != null)
			throw new RuntimeException("duplicate type" + fullname);

		//System.out.println("newtype '" + fullname + "'");
	}

	public static Type compile(Namespace space, String name) {
		return compile(space, name, null, null);
	}

	public static Type compile(Namespace space, String name, String key, String value) {
		Type type = _compile(space, name, key, value);

		if (null == type)
			type = _compile(space, space.join(".") + "." + name, key, value);

		if (null == type)
			throw new RuntimeException("ERROR bean NOT FOUND! '" + name + "'" + key + "." + value + "@" + space);

		return type;
	}

	private static Type _compile(Namespace space, String name, String key, String value) {
		Type type = types.get(name);
		if (null == type)
			return null;
		return type.compile(space, key, value);
	}

	static {
		new TypeByte(types);
		new TypeInt(types);
		new TypeLong(types);
		new TypeOctets(types);
		new TypeMap(types);
		new TypeList(types);
		new TypeVector(types);
		new TypeSet(types);
		new TypeText(types);
		new TypeFloat(types);
		new TypeShort(types);
	}
}
