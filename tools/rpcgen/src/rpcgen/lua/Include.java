package rpcgen.lua;

import java.util.HashSet;
import java.util.Set;

import rpcgen.types.Bean;
import rpcgen.types.Type;
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

class Include implements Visitor {
	private Set<String> includes = new HashSet<String>();

	public static String [] includes(Type bean) {
		Set<Type> deps = new HashSet<Type>();
		bean.depends(deps);
		deps.remove(bean);

		Include vistor = new Include(new HashSet<String>());
		for (Type type : deps)
			type.accept(vistor);

		String [] sortedIncludes = vistor.includes.toArray(new String[0]);
		java.util.Arrays.sort(sortedIncludes);
		return sortedIncludes;
	}

	public Include(Set<String> includes) {
		this.includes.addAll(includes);
	}

	public void visit(Bean bean) {
		this.includes.add(bean.getLuaRequire());
	}

	public void visit(TypeByte type) {
	}

	public void visit(TypeFloat type) {
	}

	public void visit(TypeInt type) {
	}

	public void visit(TypeShort type){
	}

	public void visit(TypeList type) {
	}

	public void visit(TypeLong type) {
	}

	public void visit(TypeMap type) {
	}

	public void visit(TypeOctets type) {
	}

	public void visit(TypeSet type) {
	}

	public void visit(TypeText type) {
	}

	public void visit(TypeVector type) {

	}

}
