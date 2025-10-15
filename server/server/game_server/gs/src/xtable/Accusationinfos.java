package xtable;

// typed table access point
public class Accusationinfos {
	Accusationinfos() {
	}

	public static xbean.AccusationInfo get(Long key) {
		return _Tables_.getInstance().accusationinfos.get(key);
	}

	public static xbean.AccusationInfo get(Long key, xbean.AccusationInfo value) {
		return _Tables_.getInstance().accusationinfos.get(key, value);
	}

	public static void insert(Long key, xbean.AccusationInfo value) {
		_Tables_.getInstance().accusationinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().accusationinfos.delete(key);
	}

	public static boolean add(Long key, xbean.AccusationInfo value) {
		return _Tables_.getInstance().accusationinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().accusationinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AccusationInfo> getCache() {
		return _Tables_.getInstance().accusationinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.AccusationInfo> getTable() {
		return _Tables_.getInstance().accusationinfos;
	}

	public static xbean.AccusationInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AccusationInfo, xbean.AccusationInfo>() {
			public xbean.AccusationInfo get(xbean.AccusationInfo v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AccusationInfo, Long>() {
				public Long get(xbean.AccusationInfo v) { return v.getRoleid(); }
			});
	}

	public static java.util.List<Long> selectAccusationedroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AccusationInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.AccusationInfo v) { return v.getAccusationedroleidsAsData(); }
			});
	}

	public static Long selectLasttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AccusationInfo, Long>() {
				public Long get(xbean.AccusationInfo v) { return v.getLasttime(); }
			});
	}

}
