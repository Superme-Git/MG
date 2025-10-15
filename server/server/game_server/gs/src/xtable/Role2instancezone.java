package xtable;

// typed table access point
public class Role2instancezone {
	Role2instancezone() {
	}

	public static Long get(Long key) {
		return _Tables_.getInstance().role2instancezone.get(key);
	}

	public static Long get(Long key, Long value) {
		return _Tables_.getInstance().role2instancezone.get(key, value);
	}

	public static void insert(Long key, Long value) {
		_Tables_.getInstance().role2instancezone.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().role2instancezone.delete(key);
	}

	public static boolean add(Long key, Long value) {
		return _Tables_.getInstance().role2instancezone.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().role2instancezone.remove(key);
	}

	public static mkdb.TTableCache<Long, Long> getCache() {
		return _Tables_.getInstance().role2instancezone.getCache();
	}

	public static mkdb.TTable<Long, Long> getTable() {
		return _Tables_.getInstance().role2instancezone;
	}

	public static Long select(Long key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
