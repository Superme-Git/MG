package mkio;

/**
 * 不管理连接的实现。
 *
 * 不对连接进行任何管理，适合用于纯被动的服务器。
 */
public class ManagerNone extends Manager {
	@Override
	protected void addMkio(Mkio mkio) {
	}

	@Override
	public Mkio get() {
		return null;
	}

	@Override
	protected void removeMkio(Mkio mkio, Throwable e) {
	}

	@Override
	public int size() {
		return 0;
	}
	
}
