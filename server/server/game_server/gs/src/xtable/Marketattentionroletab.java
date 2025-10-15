package xtable;

// typed table access point
public class Marketattentionroletab {
	Marketattentionroletab() {
	}

	public static xbean.AttentionRole get(String key) {
		return _Tables_.getInstance().marketattentionroletab.get(key);
	}

	public static xbean.AttentionRole get(String key, xbean.AttentionRole value) {
		return _Tables_.getInstance().marketattentionroletab.get(key, value);
	}

	public static void insert(String key, xbean.AttentionRole value) {
		_Tables_.getInstance().marketattentionroletab.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().marketattentionroletab.delete(key);
	}

	public static boolean add(String key, xbean.AttentionRole value) {
		return _Tables_.getInstance().marketattentionroletab.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().marketattentionroletab.remove(key);
	}

	public static mkdb.TTableCache<String, xbean.AttentionRole> getCache() {
		return _Tables_.getInstance().marketattentionroletab.getCache();
	}

	public static mkdb.TTable<String, xbean.AttentionRole> getTable() {
		return _Tables_.getInstance().marketattentionroletab;
	}

	public static xbean.AttentionRole select(String key) {
		return getTable().select(key, new mkdb.TField<xbean.AttentionRole, xbean.AttentionRole>() {
			public xbean.AttentionRole get(xbean.AttentionRole v) { return v.toData(); }
		});
	}

	public static java.util.Set<Long> selectRoleids(String key) {
		return getTable().select(key, new mkdb.TField<xbean.AttentionRole, java.util.Set<Long>>() {
				public java.util.Set<Long> get(xbean.AttentionRole v) { return v.getRoleidsAsData(); }
			});
	}

}
