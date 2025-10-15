package xtable;

// typed table access point
public class Bingfengroles {
	Bingfengroles() {
	}

	public static xbean.BingFengRole get(Long key) {
		return _Tables_.getInstance().bingfengroles.get(key);
	}

	public static xbean.BingFengRole get(Long key, xbean.BingFengRole value) {
		return _Tables_.getInstance().bingfengroles.get(key, value);
	}

	public static void insert(Long key, xbean.BingFengRole value) {
		_Tables_.getInstance().bingfengroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().bingfengroles.delete(key);
	}

	public static boolean add(Long key, xbean.BingFengRole value) {
		return _Tables_.getInstance().bingfengroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().bingfengroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BingFengRole> getCache() {
		return _Tables_.getInstance().bingfengroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.BingFengRole> getTable() {
		return _Tables_.getInstance().bingfengroles;
	}

	public static xbean.BingFengRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRole, xbean.BingFengRole>() {
			public xbean.BingFengRole get(xbean.BingFengRole v) { return v.toData(); }
		});
	}

	public static Integer selectInstzoneid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRole, Integer>() {
				public Integer get(xbean.BingFengRole v) { return v.getInstzoneid(); }
			});
	}

	public static Integer selectTimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRole, Integer>() {
				public Integer get(xbean.BingFengRole v) { return v.getTimes(); }
			});
	}

	public static Integer selectChargetimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRole, Integer>() {
				public Integer get(xbean.BingFengRole v) { return v.getChargetimes(); }
			});
	}

	public static java.util.Map<Integer, xbean.RoleBFInfo> selectInfos(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRole, java.util.Map<Integer, xbean.RoleBFInfo>>() {
				public java.util.Map<Integer, xbean.RoleBFInfo> get(xbean.BingFengRole v) { return v.getInfosAsData(); }
			});
	}

}
