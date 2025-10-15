package xtable;

// typed table access point
public class Rolename2key {
	Rolename2key() {
	}

	public static Long get(String key) {
		return _Tables_.getInstance().rolename2key.get(key);
	}

	public static Long get(String key, Long value) {
		return _Tables_.getInstance().rolename2key.get(key, value);
	}

	public static void insert(String key, Long value) {
		_Tables_.getInstance().rolename2key.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().rolename2key.delete(key);
	}

	public static boolean add(String key, Long value) {
		return _Tables_.getInstance().rolename2key.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().rolename2key.remove(key);
	}

	public static mkdb.TTableCache<String, Long> getCache() {
		return _Tables_.getInstance().rolename2key.getCache();
	}

	public static mkdb.TTable<String, Long> getTable() {
		return _Tables_.getInstance().rolename2key;
	}

	public static Long select(String key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
