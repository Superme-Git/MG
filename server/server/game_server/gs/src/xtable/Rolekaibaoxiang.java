package xtable;

// typed table access point
public class Rolekaibaoxiang {
	Rolekaibaoxiang() {
	}

	public static xbean.BaoXiangInfo get(Long key) {
		return _Tables_.getInstance().rolekaibaoxiang.get(key);
	}

	public static xbean.BaoXiangInfo get(Long key, xbean.BaoXiangInfo value) {
		return _Tables_.getInstance().rolekaibaoxiang.get(key, value);
	}

	public static void insert(Long key, xbean.BaoXiangInfo value) {
		_Tables_.getInstance().rolekaibaoxiang.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolekaibaoxiang.delete(key);
	}

	public static boolean add(Long key, xbean.BaoXiangInfo value) {
		return _Tables_.getInstance().rolekaibaoxiang.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolekaibaoxiang.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BaoXiangInfo> getCache() {
		return _Tables_.getInstance().rolekaibaoxiang.getCache();
	}

	public static mkdb.TTable<Long, xbean.BaoXiangInfo> getTable() {
		return _Tables_.getInstance().rolekaibaoxiang;
	}

	public static xbean.BaoXiangInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BaoXiangInfo, xbean.BaoXiangInfo>() {
			public xbean.BaoXiangInfo get(xbean.BaoXiangInfo v) { return v.toData(); }
		});
	}

	public static Integer selectOpentimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BaoXiangInfo, Integer>() {
				public Integer get(xbean.BaoXiangInfo v) { return v.getOpentimes(); }
			});
	}

	public static Long selectLastopentime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BaoXiangInfo, Long>() {
				public Long get(xbean.BaoXiangInfo v) { return v.getLastopentime(); }
			});
	}

}
