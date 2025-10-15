package xtable;

// typed table access point
public class Userid2forbidtime {
	Userid2forbidtime() {
	}

	public static xbean.forbidinfo get(Integer key) {
		return _Tables_.getInstance().userid2forbidtime.get(key);
	}

	public static xbean.forbidinfo get(Integer key, xbean.forbidinfo value) {
		return _Tables_.getInstance().userid2forbidtime.get(key, value);
	}

	public static void insert(Integer key, xbean.forbidinfo value) {
		_Tables_.getInstance().userid2forbidtime.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().userid2forbidtime.delete(key);
	}

	public static boolean add(Integer key, xbean.forbidinfo value) {
		return _Tables_.getInstance().userid2forbidtime.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().userid2forbidtime.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.forbidinfo> getCache() {
		return _Tables_.getInstance().userid2forbidtime.getCache();
	}

	public static mkdb.TTable<Integer, xbean.forbidinfo> getTable() {
		return _Tables_.getInstance().userid2forbidtime;
	}

	public static xbean.forbidinfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.forbidinfo, xbean.forbidinfo>() {
			public xbean.forbidinfo get(xbean.forbidinfo v) { return v.toData(); }
		});
	}

	public static Long selectForbidtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.forbidinfo, Long>() {
				public Long get(xbean.forbidinfo v) { return v.getForbidtime(); }
			});
	}

	public static String selectReason(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.forbidinfo, String>() {
				public String get(xbean.forbidinfo v) { return v.getReason(); }
			});
	}

}
