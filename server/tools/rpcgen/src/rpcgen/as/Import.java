package rpcgen.as;

import java.util.HashSet;
import java.util.Set;

import rpcgen.Protocol;
import rpcgen.Rpc;
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

class Import implements Visitor {
	private Set<String> depends = new HashSet<String>();

	public static String [] imports(Type bean) {
		Set<Type> deps = new HashSet<Type>();
		bean.depends(deps);
		deps.remove(bean);
		return imports(deps);
	}

	public static String [] imports(Protocol protocol) {
		return imports(protocol.getImplementBean());
	}

	public static String [] imports(Rpc rpc) {
		Set<Type> deps = new HashSet<Type>();
		deps.add(rpc.getArgumentBean());
		deps.add(rpc.getResultBean());

		rpc.getImplementBean().depends(deps);
		deps.remove(rpc.getImplementBean());
		return imports(deps);
	}

	public static String [] imports(Set<Type> deps) {
		Import vistor = new Import(new HashSet<String>());
		for (Type type : deps)
			type.accept(vistor);

		String [] sortedImports = vistor.depends.toArray(new String[0]);
		java.util.Arrays.sort(sortedImports);
		return sortedImports;
	}

	public Import(Set<String> includes) {
		this.depends.addAll(includes);
	}

	@Override
	public void visit(Bean bean) {
		this.depends.add("import " + bean.fullName() + ";");
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
	}

	@Override
	public void visit(TypeLong type) {
		this.depends.add("import com.locojoy.types.Int64;");
	}

	@Override
	public void visit(TypeMap type) {
	}

	@Override
	public void visit(TypeOctets type) {
		this.depends.add("import com.locojoy.types.Octets;");
	}

	@Override
	public void visit(TypeSet type) {
	}

	@Override
	public void visit(TypeText type) {
	}

	@Override
	public void visit(TypeVector type) {
	}

}
