package xtable;

// typed table access point
public class Platorderhistroy {
	Platorderhistroy() {
	}

	public static Long get(String key) {
		return _Tables_.getInstance().platorderhistroy.get(key);
	}

	public static Long get(String key, Long value) {
		return _Tables_.getInstance().platorderhistroy.get(key, value);
	}

	public static void insert(String key, Long value) {
		_Tables_.getInstance().platorderhistroy.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().platorderhistroy.delete(key);
	}

	public static boolean add(String key, Long value) {
		return _Tables_.getInstance().platorderhistroy.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().platorderhistroy.remove(key);
	}

	public static mkdb.TTableCache<String, Long> getCache() {
		return _Tables_.getInstance().platorderhistroy.getCache();
	}

	public static mkdb.TTable<String, Long> getTable() {
		return _Tables_.getInstance().platorderhistroy;
	}

	public static Long select(String key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
