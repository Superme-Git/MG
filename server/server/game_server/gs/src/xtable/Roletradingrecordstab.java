package xtable;

// typed table access point
public class Roletradingrecordstab {
	Roletradingrecordstab() {
	}

	public static xbean.RoleTradingRecord get(String key) {
		return _Tables_.getInstance().roletradingrecordstab.get(key);
	}

	public static xbean.RoleTradingRecord get(String key, xbean.RoleTradingRecord value) {
		return _Tables_.getInstance().roletradingrecordstab.get(key, value);
	}

	public static void insert(String key, xbean.RoleTradingRecord value) {
		_Tables_.getInstance().roletradingrecordstab.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().roletradingrecordstab.delete(key);
	}

	public static boolean add(String key, xbean.RoleTradingRecord value) {
		return _Tables_.getInstance().roletradingrecordstab.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().roletradingrecordstab.remove(key);
	}

	public static mkdb.TTableCache<String, xbean.RoleTradingRecord> getCache() {
		return _Tables_.getInstance().roletradingrecordstab.getCache();
	}

	public static mkdb.TTable<String, xbean.RoleTradingRecord> getTable() {
		return _Tables_.getInstance().roletradingrecordstab;
	}

	public static xbean.RoleTradingRecord select(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, xbean.RoleTradingRecord>() {
			public xbean.RoleTradingRecord get(xbean.RoleTradingRecord v) { return v.toData(); }
		});
	}

	public static String selectTradingid(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, String>() {
				public String get(xbean.RoleTradingRecord v) { return v.getTradingid(); }
			});
	}

	public static Long selectRoleid(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Long>() {
				public Long get(xbean.RoleTradingRecord v) { return v.getRoleid(); }
			});
	}

	public static Integer selectTradingtype(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Integer>() {
				public Integer get(xbean.RoleTradingRecord v) { return v.getTradingtype(); }
			});
	}

	public static Integer selectCurnum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Integer>() {
				public Integer get(xbean.RoleTradingRecord v) { return v.getCurnum(); }
			});
	}

	public static Integer selectAllnum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Integer>() {
				public Integer get(xbean.RoleTradingRecord v) { return v.getAllnum(); }
			});
	}

	public static Integer selectPrice(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Integer>() {
				public Integer get(xbean.RoleTradingRecord v) { return v.getPrice(); }
			});
	}

	public static Long selectCreatetime(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Long>() {
				public Long get(xbean.RoleTradingRecord v) { return v.getCreatetime(); }
			});
	}

	public static Long selectTradingtime(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Long>() {
				public Long get(xbean.RoleTradingRecord v) { return v.getTradingtime(); }
			});
	}

	public static Integer selectState(String key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingRecord, Integer>() {
				public Integer get(xbean.RoleTradingRecord v) { return v.getState(); }
			});
	}

}
