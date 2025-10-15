package rpcgen.types;

public interface Visitor {
	public void visit(Bean type);

	public void visit(TypeByte type);
	public void visit(TypeInt type);
	public void visit(TypeLong type);

	public void visit(TypeOctets type);
	public void visit(TypeText type);

	public void visit(TypeList type);
	public void visit(TypeVector type);
	public void visit(TypeSet type);
	public void visit(TypeMap type);

	public void visit(TypeFloat type);
	public void visit(TypeShort type);
}
