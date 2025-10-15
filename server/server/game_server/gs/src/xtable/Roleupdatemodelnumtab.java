package xtable;

// typed table access point
public class Roleupdatemodelnumtab {
	Roleupdatemodelnumtab() {
	}

	public static xbean.RoleUpdateModelNum get(Long key) {
		return _Tables_.getInstance().roleupdatemodelnumtab.get(key);
	}

	public static xbean.RoleUpdateModelNum get(Long key, xbean.RoleUpdateModelNum value) {
		return _Tables_.getInstance().roleupdatemodelnumtab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleUpdateModelNum value) {
		_Tables_.getInstance().roleupdatemodelnumtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleupdatemodelnumtab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleUpdateModelNum value) {
		return _Tables_.getInstance().roleupdatemodelnumtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleupdatemodelnumtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleUpdateModelNum> getCache() {
		return _Tables_.getInstance().roleupdatemodelnumtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleUpdateModelNum> getTable() {
		return _Tables_.getInstance().roleupdatemodelnumtab;
	}

	public static xbean.RoleUpdateModelNum select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleUpdateModelNum, xbean.RoleUpdateModelNum>() {
			public xbean.RoleUpdateModelNum get(xbean.RoleUpdateModelNum v) { return v.toData(); }
		});
	}

	public static Integer selectTradingbuyfushinum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleUpdateModelNum, Integer>() {
				public Integer get(xbean.RoleUpdateModelNum v) { return v.getTradingbuyfushinum(); }
			});
	}

}
