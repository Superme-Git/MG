package mkdb;

public interface Log {
	public void rollback();
	public void commit();
}
