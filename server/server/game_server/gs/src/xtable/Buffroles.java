package xtable;

// typed table access point
public class Buffroles {
	Buffroles() {
	}

	public static xbean.BuffRole get(Long key) {
		return _Tables_.getInstance().buffroles.get(key);
	}

	public static xbean.BuffRole get(Long key, xbean.BuffRole value) {
		return _Tables_.getInstance().buffroles.get(key, value);
	}

	public static void insert(Long key, xbean.BuffRole value) {
		_Tables_.getInstance().buffroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().buffroles.delete(key);
	}

	public static boolean add(Long key, xbean.BuffRole value) {
		return _Tables_.getInstance().buffroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().buffroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BuffRole> getCache() {
		return _Tables_.getInstance().buffroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.BuffRole> getTable() {
		return _Tables_.getInstance().buffroles;
	}

	public static xbean.BuffRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuffRole, xbean.BuffRole>() {
			public xbean.BuffRole get(xbean.BuffRole v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuffRole, Long>() {
				public Long get(xbean.BuffRole v) { return v.getRoleid(); }
			});
	}

	public static xbean.BuffAgent selectBuffagent(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuffRole, xbean.BuffAgent>() {
				public xbean.BuffAgent get(xbean.BuffRole v) { return v.getBuffagent(); }
			});
	}

}
