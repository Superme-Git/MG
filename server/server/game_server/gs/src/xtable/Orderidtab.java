package xtable;

// typed table access point
public class Orderidtab {
	Orderidtab() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().orderidtab.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(Long value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static Long get(Long key) {
		return _Tables_.getInstance().orderidtab.get(key);
	}

	public static Long get(Long key, Long value) {
		return _Tables_.getInstance().orderidtab.get(key, value);
	}

	public static void insert(Long key, Long value) {
		_Tables_.getInstance().orderidtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().orderidtab.delete(key);
	}

	public static boolean add(Long key, Long value) {
		return _Tables_.getInstance().orderidtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().orderidtab.remove(key);
	}

	public static mkdb.TTableCache<Long, Long> getCache() {
		return _Tables_.getInstance().orderidtab.getCache();
	}

	public static mkdb.TTable<Long, Long> getTable() {
		return _Tables_.getInstance().orderidtab;
	}

	public static Long select(Long key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
