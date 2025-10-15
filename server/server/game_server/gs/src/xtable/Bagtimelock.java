package xtable;

// typed table access point
public class Bagtimelock {
	Bagtimelock() {
	}

	public static xbean.BagTimeLock get(Long key) {
		return _Tables_.getInstance().bagtimelock.get(key);
	}

	public static xbean.BagTimeLock get(Long key, xbean.BagTimeLock value) {
		return _Tables_.getInstance().bagtimelock.get(key, value);
	}

	public static void insert(Long key, xbean.BagTimeLock value) {
		_Tables_.getInstance().bagtimelock.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().bagtimelock.delete(key);
	}

	public static boolean add(Long key, xbean.BagTimeLock value) {
		return _Tables_.getInstance().bagtimelock.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().bagtimelock.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BagTimeLock> getCache() {
		return _Tables_.getInstance().bagtimelock.getCache();
	}

	public static mkdb.TTable<Long, xbean.BagTimeLock> getTable() {
		return _Tables_.getInstance().bagtimelock;
	}

	public static xbean.BagTimeLock select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BagTimeLock, xbean.BagTimeLock>() {
			public xbean.BagTimeLock get(xbean.BagTimeLock v) { return v.toData(); }
		});
	}

	public static Integer selectItemkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BagTimeLock, Integer>() {
				public Integer get(xbean.BagTimeLock v) { return v.getItemkey(); }
			});
	}

	public static Integer selectNum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BagTimeLock, Integer>() {
				public Integer get(xbean.BagTimeLock v) { return v.getNum(); }
			});
	}

	public static Long selectTimeout(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BagTimeLock, Long>() {
				public Long get(xbean.BagTimeLock v) { return v.getTimeout(); }
			});
	}

}
