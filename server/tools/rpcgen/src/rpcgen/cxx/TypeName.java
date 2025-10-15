package rpcgen.cxx;

import rpcgen.types.*;

class TypeName implements Visitor {
	private String name;

	public static String getName(rpcgen.types.Type type) {
		TypeName visitor = new TypeName();
		type.accept(visitor);
		return visitor.name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void visit(Bean type) {
		name = (type.getSpace() + "." + type.getName()).replaceAll("\\.", "::");
	}

	@Override
	public void visit(TypeByte type) {
		name = "char";
	}

	@Override
	public void visit(TypeInt type) {
		name = "int";
	}

	@Override
	public void visit(TypeShort type)
	{
		name = "short";		
	}

	@Override
	public void visit(TypeLong type) {
		name = "int64_t";
	}

	@Override
	public void visit(TypeOctets type) {
		name = "GNET::Octets";
	}

	@Override
	public void visit(TypeText type) {
		name = "GNET::Octets";
	}

	@Override
	public void visit(TypeList type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::list<" + valuename + ">";
	}

	@Override
	public void visit(TypeVector type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::vector<" + valuename + ">";
	}

	@Override
	public void visit(TypeSet type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::set<" + valuename + ">";
	}

	@Override
	public void visit(TypeMap type) {
		String keyname = BoxingName.getName(type.getKeytype());
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::map<" + keyname + ", " + valuename + ">";
	}

	@Override
	public void visit(TypeFloat type) {
		name = "float";
	}

}
