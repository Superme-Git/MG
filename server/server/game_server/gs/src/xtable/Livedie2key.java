package xtable;

// typed table access point
public class Livedie2key {
	Livedie2key() {
	}

	public static Long get(Long key) {
		return _Tables_.getInstance().livedie2key.get(key);
	}

	public static Long get(Long key, Long value) {
		return _Tables_.getInstance().livedie2key.get(key, value);
	}

	public static void insert(Long key, Long value) {
		_Tables_.getInstance().livedie2key.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().livedie2key.delete(key);
	}

	public static boolean add(Long key, Long value) {
		return _Tables_.getInstance().livedie2key.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().livedie2key.remove(key);
	}

	public static mkdb.TTableCache<Long, Long> getCache() {
		return _Tables_.getInstance().livedie2key.getCache();
	}

	public static mkdb.TTable<Long, Long> getTable() {
		return _Tables_.getInstance().livedie2key;
	}

	public static Long select(Long key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
