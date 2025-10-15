package xtable;

// typed table access point
public class Appstoretidstatus {
	Appstoretidstatus() {
	}

	public static xbean.AppstoretidStatus get(Long key) {
		return _Tables_.getInstance().appstoretidstatus.get(key);
	}

	public static xbean.AppstoretidStatus get(Long key, xbean.AppstoretidStatus value) {
		return _Tables_.getInstance().appstoretidstatus.get(key, value);
	}

	public static void insert(Long key, xbean.AppstoretidStatus value) {
		_Tables_.getInstance().appstoretidstatus.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().appstoretidstatus.delete(key);
	}

	public static boolean add(Long key, xbean.AppstoretidStatus value) {
		return _Tables_.getInstance().appstoretidstatus.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().appstoretidstatus.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AppstoretidStatus> getCache() {
		return _Tables_.getInstance().appstoretidstatus.getCache();
	}

	public static mkdb.TTable<Long, xbean.AppstoretidStatus> getTable() {
		return _Tables_.getInstance().appstoretidstatus;
	}

	public static xbean.AppstoretidStatus select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AppstoretidStatus, xbean.AppstoretidStatus>() {
			public xbean.AppstoretidStatus get(xbean.AppstoretidStatus v) { return v.toData(); }
		});
	}

	public static Long selectChargesn(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AppstoretidStatus, Long>() {
				public Long get(xbean.AppstoretidStatus v) { return v.getChargesn(); }
			});
	}

	public static Integer selectStatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AppstoretidStatus, Integer>() {
				public Integer get(xbean.AppstoretidStatus v) { return v.getStatus(); }
			});
	}

}
