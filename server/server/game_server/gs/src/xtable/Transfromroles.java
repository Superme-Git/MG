package xtable;

// typed table access point
public class Transfromroles {
	Transfromroles() {
	}

	public static xbean.TransfromInfo get(Long key) {
		return _Tables_.getInstance().transfromroles.get(key);
	}

	public static xbean.TransfromInfo get(Long key, xbean.TransfromInfo value) {
		return _Tables_.getInstance().transfromroles.get(key, value);
	}

	public static void insert(Long key, xbean.TransfromInfo value) {
		_Tables_.getInstance().transfromroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().transfromroles.delete(key);
	}

	public static boolean add(Long key, xbean.TransfromInfo value) {
		return _Tables_.getInstance().transfromroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().transfromroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TransfromInfo> getCache() {
		return _Tables_.getInstance().transfromroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.TransfromInfo> getTable() {
		return _Tables_.getInstance().transfromroles;
	}

	public static xbean.TransfromInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromInfo, xbean.TransfromInfo>() {
			public xbean.TransfromInfo get(xbean.TransfromInfo v) { return v.toData(); }
		});
	}

	public static Integer selectTransformid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromInfo, Integer>() {
				public Integer get(xbean.TransfromInfo v) { return v.getTransformid(); }
			});
	}

	public static Integer selectQuestid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromInfo, Integer>() {
				public Integer get(xbean.TransfromInfo v) { return v.getQuestid(); }
			});
	}

	public static Long selectValiddate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromInfo, Long>() {
				public Long get(xbean.TransfromInfo v) { return v.getValiddate(); }
			});
	}

	public static Integer selectRide(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromInfo, Integer>() {
				public Integer get(xbean.TransfromInfo v) { return v.getRide(); }
			});
	}

}
