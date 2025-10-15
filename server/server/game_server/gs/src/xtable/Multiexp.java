package xtable;

// typed table access point
public class Multiexp {
	Multiexp() {
	}

	public static xbean.MultiExpInfo get(Long key) {
		return _Tables_.getInstance().multiexp.get(key);
	}

	public static xbean.MultiExpInfo get(Long key, xbean.MultiExpInfo value) {
		return _Tables_.getInstance().multiexp.get(key, value);
	}

	public static void insert(Long key, xbean.MultiExpInfo value) {
		_Tables_.getInstance().multiexp.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().multiexp.delete(key);
	}

	public static boolean add(Long key, xbean.MultiExpInfo value) {
		return _Tables_.getInstance().multiexp.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().multiexp.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MultiExpInfo> getCache() {
		return _Tables_.getInstance().multiexp.getCache();
	}

	public static mkdb.TTable<Long, xbean.MultiExpInfo> getTable() {
		return _Tables_.getInstance().multiexp;
	}

	public static xbean.MultiExpInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, xbean.MultiExpInfo>() {
			public xbean.MultiExpInfo get(xbean.MultiExpInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectMultiexptime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.MultiExpInfo v) { return v.getMultiexptimeAsData(); }
			});
	}

	public static Integer selectRemainnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, Integer>() {
				public Integer get(xbean.MultiExpInfo v) { return v.getRemainnum(); }
			});
	}

	public static Integer selectDupdannum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, Integer>() {
				public Integer get(xbean.MultiExpInfo v) { return v.getDupdannum(); }
			});
	}

	public static Long selectLastbacktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, Long>() {
				public Long get(xbean.MultiExpInfo v) { return v.getLastbacktime(); }
			});
	}

	public static Long selectRefreshtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MultiExpInfo, Long>() {
				public Long get(xbean.MultiExpInfo v) { return v.getRefreshtime(); }
			});
	}

}
