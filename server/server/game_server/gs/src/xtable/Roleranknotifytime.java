package xtable;

// typed table access point
public class Roleranknotifytime {
	Roleranknotifytime() {
	}

	public static xbean.RoleRankNotifyTimeInfo get(Long key) {
		return _Tables_.getInstance().roleranknotifytime.get(key);
	}

	public static xbean.RoleRankNotifyTimeInfo get(Long key, xbean.RoleRankNotifyTimeInfo value) {
		return _Tables_.getInstance().roleranknotifytime.get(key, value);
	}

	public static void insert(Long key, xbean.RoleRankNotifyTimeInfo value) {
		_Tables_.getInstance().roleranknotifytime.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleranknotifytime.delete(key);
	}

	public static boolean add(Long key, xbean.RoleRankNotifyTimeInfo value) {
		return _Tables_.getInstance().roleranknotifytime.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleranknotifytime.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleRankNotifyTimeInfo> getCache() {
		return _Tables_.getInstance().roleranknotifytime.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleRankNotifyTimeInfo> getTable() {
		return _Tables_.getInstance().roleranknotifytime;
	}

	public static xbean.RoleRankNotifyTimeInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRankNotifyTimeInfo, xbean.RoleRankNotifyTimeInfo>() {
			public xbean.RoleRankNotifyTimeInfo get(xbean.RoleRankNotifyTimeInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectLasttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRankNotifyTimeInfo, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.RoleRankNotifyTimeInfo v) { return v.getLasttimeAsData(); }
			});
	}

}
