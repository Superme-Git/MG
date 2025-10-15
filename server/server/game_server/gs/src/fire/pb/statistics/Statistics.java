package fire.pb.statistics;


public abstract class Statistics<T extends mkdb.Bean> {
	final boolean readonly;
	final long roleid;
	T statistics;
	public Statistics( 
			final long roleid, 
			final boolean readonly ) {
		this.roleid = roleid;
		this.readonly = readonly;
		final T data = getStatistics();
		if (data == null) {
			newStatistics();
		} else {
			statistics = data;
		}
	}
	/**
	 * 获得xdb表数据
	 * @return
	 */
	protected abstract T getStatistics();
	protected abstract void newStatistics();
}
