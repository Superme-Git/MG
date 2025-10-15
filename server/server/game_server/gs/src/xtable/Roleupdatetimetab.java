package xtable;

// typed table access point
public class Roleupdatetimetab {
	Roleupdatetimetab() {
	}

	public static xbean.RoleUpdateTime get(Long key) {
		return _Tables_.getInstance().roleupdatetimetab.get(key);
	}

	public static xbean.RoleUpdateTime get(Long key, xbean.RoleUpdateTime value) {
		return _Tables_.getInstance().roleupdatetimetab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleUpdateTime value) {
		_Tables_.getInstance().roleupdatetimetab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleupdatetimetab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleUpdateTime value) {
		return _Tables_.getInstance().roleupdatetimetab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleupdatetimetab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleUpdateTime> getCache() {
		return _Tables_.getInstance().roleupdatetimetab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleUpdateTime> getTable() {
		return _Tables_.getInstance().roleupdatetimetab;
	}

	public static xbean.RoleUpdateTime select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleUpdateTime, xbean.RoleUpdateTime>() {
			public xbean.RoleUpdateTime get(xbean.RoleUpdateTime v) { return v.toData(); }
		});
	}

	public static Long selectDateupdatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleUpdateTime, Long>() {
				public Long get(xbean.RoleUpdateTime v) { return v.getDateupdatetime(); }
			});
	}

	public static Long selectWeekupdatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleUpdateTime, Long>() {
				public Long get(xbean.RoleUpdateTime v) { return v.getWeekupdatetime(); }
			});
	}

}
