package xtable;

// typed table access point
public class Anyemaxituan {
	Anyemaxituan() {
	}

	public static xbean.AnYeData get(Long key) {
		return _Tables_.getInstance().anyemaxituan.get(key);
	}

	public static xbean.AnYeData get(Long key, xbean.AnYeData value) {
		return _Tables_.getInstance().anyemaxituan.get(key, value);
	}

	public static void insert(Long key, xbean.AnYeData value) {
		_Tables_.getInstance().anyemaxituan.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().anyemaxituan.delete(key);
	}

	public static boolean add(Long key, xbean.AnYeData value) {
		return _Tables_.getInstance().anyemaxituan.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().anyemaxituan.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AnYeData> getCache() {
		return _Tables_.getInstance().anyemaxituan.getCache();
	}

	public static mkdb.TTable<Long, xbean.AnYeData> getTable() {
		return _Tables_.getInstance().anyemaxituan;
	}

	public static xbean.AnYeData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, xbean.AnYeData>() {
			public xbean.AnYeData get(xbean.AnYeData v) { return v.toData(); }
		});
	}

	public static Long selectJointime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Long>() {
				public Long get(xbean.AnYeData v) { return v.getJointime(); }
			});
	}

	public static Long selectRandomtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Long>() {
				public Long get(xbean.AnYeData v) { return v.getRandomtime(); }
			});
	}

	public static Long selectRefreshtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Long>() {
				public Long get(xbean.AnYeData v) { return v.getRefreshtime(); }
			});
	}

	public static Integer selectTimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getTimes(); }
			});
	}

	public static Integer selectTodaynum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getTodaynum(); }
			});
	}

	public static Long selectToday(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Long>() {
				public Long get(xbean.AnYeData v) { return v.getToday(); }
			});
	}

	public static Integer selectRenxins(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getRenxins(); }
			});
	}

	public static Integer selectReqhelptimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getReqhelptimes(); }
			});
	}

	public static Integer selectHelptimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getHelptimes(); }
			});
	}

	public static Integer selectLegendtask(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, Integer>() {
				public Integer get(xbean.AnYeData v) { return v.getLegendtask(); }
			});
	}

	public static java.util.Map<Integer, xbean.AnYeTask> selectAnyetasks(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AnYeData, java.util.Map<Integer, xbean.AnYeTask>>() {
				public java.util.Map<Integer, xbean.AnYeTask> get(xbean.AnYeData v) { return v.getAnyetasksAsData(); }
			});
	}

}
