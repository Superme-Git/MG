package xtable;

// typed table access point
public class Progressbartable {
	Progressbartable() {
	}

	public static xbean.ProgressBarInfo get(Long key) {
		return _Tables_.getInstance().progressbartable.get(key);
	}

	public static xbean.ProgressBarInfo get(Long key, xbean.ProgressBarInfo value) {
		return _Tables_.getInstance().progressbartable.get(key, value);
	}

	public static void insert(Long key, xbean.ProgressBarInfo value) {
		_Tables_.getInstance().progressbartable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().progressbartable.delete(key);
	}

	public static boolean add(Long key, xbean.ProgressBarInfo value) {
		return _Tables_.getInstance().progressbartable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().progressbartable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ProgressBarInfo> getCache() {
		return _Tables_.getInstance().progressbartable.getCache();
	}

	public static mkdb.TTable<Long, xbean.ProgressBarInfo> getTable() {
		return _Tables_.getInstance().progressbartable;
	}

	public static xbean.ProgressBarInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProgressBarInfo, xbean.ProgressBarInfo>() {
			public xbean.ProgressBarInfo get(xbean.ProgressBarInfo v) { return v.toData(); }
		});
	}

	public static Integer selectBartype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProgressBarInfo, Integer>() {
				public Integer get(xbean.ProgressBarInfo v) { return v.getBartype(); }
			});
	}

	public static Integer selectItemkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProgressBarInfo, Integer>() {
				public Integer get(xbean.ProgressBarInfo v) { return v.getItemkey(); }
			});
	}

	public static Integer selectInprogress(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProgressBarInfo, Integer>() {
				public Integer get(xbean.ProgressBarInfo v) { return v.getInprogress(); }
			});
	}

	public static Long selectStarttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProgressBarInfo, Long>() {
				public Long get(xbean.ProgressBarInfo v) { return v.getStarttime(); }
			});
	}

}
