package xtable;

// typed table access point
public class Uniquepets {
	Uniquepets() {
	}

	public static xbean.UniquePet get(Long key) {
		return _Tables_.getInstance().uniquepets.get(key);
	}

	public static xbean.UniquePet get(Long key, xbean.UniquePet value) {
		return _Tables_.getInstance().uniquepets.get(key, value);
	}

	public static void insert(Long key, xbean.UniquePet value) {
		_Tables_.getInstance().uniquepets.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().uniquepets.delete(key);
	}

	public static boolean add(Long key, xbean.UniquePet value) {
		return _Tables_.getInstance().uniquepets.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().uniquepets.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.UniquePet> getCache() {
		return _Tables_.getInstance().uniquepets.getCache();
	}

	public static mkdb.TTable<Long, xbean.UniquePet> getTable() {
		return _Tables_.getInstance().uniquepets;
	}

	public static xbean.UniquePet select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.UniquePet, xbean.UniquePet>() {
			public xbean.UniquePet get(xbean.UniquePet v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.UniquePet, Long>() {
				public Long get(xbean.UniquePet v) { return v.getRoleid(); }
			});
	}

}
