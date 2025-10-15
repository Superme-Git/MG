package xtable;

// typed table access point
public class Requestgoto {
	Requestgoto() {
	}

	public static Long get(Long key) {
		return _Tables_.getInstance().requestgoto.get(key);
	}

	public static Long get(Long key, Long value) {
		return _Tables_.getInstance().requestgoto.get(key, value);
	}

	public static void insert(Long key, Long value) {
		_Tables_.getInstance().requestgoto.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().requestgoto.delete(key);
	}

	public static boolean add(Long key, Long value) {
		return _Tables_.getInstance().requestgoto.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().requestgoto.remove(key);
	}

	public static mkdb.TTableCache<Long, Long> getCache() {
		return _Tables_.getInstance().requestgoto.getCache();
	}

	public static mkdb.TTable<Long, Long> getTable() {
		return _Tables_.getInstance().requestgoto;
	}

	public static Long select(Long key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
