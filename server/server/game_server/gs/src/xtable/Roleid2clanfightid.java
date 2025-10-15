package xtable;

// typed table access point
public class Roleid2clanfightid {
	Roleid2clanfightid() {
	}

	public static Long get(Long key) {
		return _Tables_.getInstance().roleid2clanfightid.get(key);
	}

	public static Long get(Long key, Long value) {
		return _Tables_.getInstance().roleid2clanfightid.get(key, value);
	}

	public static void insert(Long key, Long value) {
		_Tables_.getInstance().roleid2clanfightid.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2clanfightid.delete(key);
	}

	public static boolean add(Long key, Long value) {
		return _Tables_.getInstance().roleid2clanfightid.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2clanfightid.remove(key);
	}

	public static mkdb.TTableCache<Long, Long> getCache() {
		return _Tables_.getInstance().roleid2clanfightid.getCache();
	}

	public static mkdb.TTable<Long, Long> getTable() {
		return _Tables_.getInstance().roleid2clanfightid;
	}

	public static Long select(Long key) {
		return getTable().select(key, new mkdb.TField<Long, Long>() {
			public Long get(Long v) { return v; }
		});
	}

}
