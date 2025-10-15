package rpcgen.cxx;

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

	@Override
	public void visit(Bean bean) {
		this.includes.add(bean.getInclude());
	}

	@Override
	public void visit(TypeByte type) {
	}

	@Override
	public void visit(TypeFloat type) {
	}

	@Override
	public void visit(TypeInt type) {
	}

	@Override
	public void visit(TypeShort type)
	{

	}

	@Override
	public void visit(TypeList type) {
		this.includes.add("#include <list>");
	}

	@Override
	public void visit(TypeLong type) {
	}

	@Override
	public void visit(TypeMap type) {
		this.includes.add("#include <map>");
	}

	@Override
	public void visit(TypeOctets type) {
	}

	@Override
	public void visit(TypeSet type) {
		this.includes.add("#include <set>");
	}

	@Override
	public void visit(TypeText type) {
	}

	@Override
	public void visit(TypeVector type) {
		this.includes.add("#include <vector>");
	}

}
