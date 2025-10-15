package xtable;

// typed table access point
public class Rolespaces {
	Rolespaces() {
	}

	public static xbean.RoleSpace get(Long key) {
		return _Tables_.getInstance().rolespaces.get(key);
	}

	public static xbean.RoleSpace get(Long key, xbean.RoleSpace value) {
		return _Tables_.getInstance().rolespaces.get(key, value);
	}

	public static void insert(Long key, xbean.RoleSpace value) {
		_Tables_.getInstance().rolespaces.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolespaces.delete(key);
	}

	public static boolean add(Long key, xbean.RoleSpace value) {
		return _Tables_.getInstance().rolespaces.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolespaces.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleSpace> getCache() {
		return _Tables_.getInstance().rolespaces.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleSpace> getTable() {
		return _Tables_.getInstance().rolespaces;
	}

	public static xbean.RoleSpace select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, xbean.RoleSpace>() {
			public xbean.RoleSpace get(xbean.RoleSpace v) { return v.toData(); }
		});
	}

	public static Integer selectGift(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, Integer>() {
				public Integer get(xbean.RoleSpace v) { return v.getGift(); }
			});
	}

	public static Integer selectPopularity(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, Integer>() {
				public Integer get(xbean.RoleSpace v) { return v.getPopularity(); }
			});
	}

	public static Integer selectRecvgift(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, Integer>() {
				public Integer get(xbean.RoleSpace v) { return v.getRecvgift(); }
			});
	}

	public static Integer selectGetgiftnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, Integer>() {
				public Integer get(xbean.RoleSpace v) { return v.getGetgiftnum(); }
			});
	}

	public static Long selectGetgifttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSpace, Long>() {
				public Long get(xbean.RoleSpace v) { return v.getGetgifttime(); }
			});
	}

}
