package rpcgen;

public interface Name {
	public String getName();
	public Namespace getSpace();
	public void setSpace(Namespace space);

	// helper
	public String fullName();
	public String fullName(String sep);

	public void compile();
}
