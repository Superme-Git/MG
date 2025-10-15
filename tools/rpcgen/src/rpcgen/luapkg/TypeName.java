package rpcgen.luapkg;

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

	public void visit(Bean type) {
		name = (type.getSpace() + "." + type.getName()).replaceAll("\\.", "::");
	}

	public void visit(TypeByte type) {
		name = "char";
	}

	public void visit(TypeInt type) {
		name = "int";
	}

	public void visit(TypeShort type)
	{
		name = "short";		
	}

	public void visit(TypeLong type) {
		name = "int64_t";
	}

	public void visit(TypeOctets type) {
		name = "FireNet::Octets";
	}

	public void visit(TypeText type) {
		name = "std::wstring";
	}

	public void visit(TypeList type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::list<" + valuename + ">";
	}

	public void visit(TypeVector type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::vector<" + valuename + ">";
	}

	public void visit(TypeSet type) {
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::set<" + valuename + ">";
	}

	public void visit(TypeMap type) {
		String keyname = BoxingName.getName(type.getKeytype());
		String valuename = BoxingName.getName(type.getValuetype());
		name = "std::map<" + keyname + ", " + valuename + ">";
	}

	public void visit(TypeFloat type) {
		name = "float";
	}

}
