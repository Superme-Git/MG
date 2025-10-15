package xtable;

// typed table access point
public class Activitytable {
	Activitytable() {
	}

	public static xbean.ActivityStatus get(Integer key) {
		return _Tables_.getInstance().activitytable.get(key);
	}

	public static xbean.ActivityStatus get(Integer key, xbean.ActivityStatus value) {
		return _Tables_.getInstance().activitytable.get(key, value);
	}

	public static void insert(Integer key, xbean.ActivityStatus value) {
		_Tables_.getInstance().activitytable.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().activitytable.delete(key);
	}

	public static boolean add(Integer key, xbean.ActivityStatus value) {
		return _Tables_.getInstance().activitytable.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().activitytable.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ActivityStatus> getCache() {
		return _Tables_.getInstance().activitytable.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ActivityStatus> getTable() {
		return _Tables_.getInstance().activitytable;
	}

	public static xbean.ActivityStatus select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, xbean.ActivityStatus>() {
			public xbean.ActivityStatus get(xbean.ActivityStatus v) { return v.toData(); }
		});
	}

	public static Integer selectId(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Integer>() {
				public Integer get(xbean.ActivityStatus v) { return v.getId(); }
			});
	}

	public static Integer selectBaseid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Integer>() {
				public Integer get(xbean.ActivityStatus v) { return v.getBaseid(); }
			});
	}

	public static Long selectInistarttime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Long>() {
				public Long get(xbean.ActivityStatus v) { return v.getInistarttime(); }
			});
	}

	public static Long selectIniendtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Long>() {
				public Long get(xbean.ActivityStatus v) { return v.getIniendtime(); }
			});
	}

	public static Long selectLaststarttime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Long>() {
				public Long get(xbean.ActivityStatus v) { return v.getLaststarttime(); }
			});
	}

	public static Long selectLastendtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Long>() {
				public Long get(xbean.ActivityStatus v) { return v.getLastendtime(); }
			});
	}

	public static Long selectCosttime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityStatus, Long>() {
				public Long get(xbean.ActivityStatus v) { return v.getCosttime(); }
			});
	}

}
