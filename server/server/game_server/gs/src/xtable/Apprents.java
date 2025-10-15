package xtable;

// typed table access point
public class Apprents {
	Apprents() {
	}

	public static xbean.MenstorInfo get(Long key) {
		return _Tables_.getInstance().apprents.get(key);
	}

	public static xbean.MenstorInfo get(Long key, xbean.MenstorInfo value) {
		return _Tables_.getInstance().apprents.get(key, value);
	}

	public static void insert(Long key, xbean.MenstorInfo value) {
		_Tables_.getInstance().apprents.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().apprents.delete(key);
	}

	public static boolean add(Long key, xbean.MenstorInfo value) {
		return _Tables_.getInstance().apprents.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().apprents.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MenstorInfo> getCache() {
		return _Tables_.getInstance().apprents.getCache();
	}

	public static mkdb.TTable<Long, xbean.MenstorInfo> getTable() {
		return _Tables_.getInstance().apprents;
	}

	public static xbean.MenstorInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorInfo, xbean.MenstorInfo>() {
			public xbean.MenstorInfo get(xbean.MenstorInfo v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.CurrApprent> selectCurrapprentices(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorInfo, java.util.List<xbean.CurrApprent>>() {
				public java.util.List<xbean.CurrApprent> get(xbean.MenstorInfo v) { return v.getCurrapprenticesAsData(); }
			});
	}

	public static java.util.List<xbean.HasApprent> selectApprentices(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorInfo, java.util.List<xbean.HasApprent>>() {
				public java.util.List<xbean.HasApprent> get(xbean.MenstorInfo v) { return v.getApprenticesAsData(); }
			});
	}

	public static Long selectUpdatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorInfo, Long>() {
				public Long get(xbean.MenstorInfo v) { return v.getUpdatetime(); }
			});
	}

}
