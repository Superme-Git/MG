package xtable;

// typed table access point
public class Roledoublecharge {
	Roledoublecharge() {
	}

	public static xbean.RoleDoubleChareInfo get(Long key) {
		return _Tables_.getInstance().roledoublecharge.get(key);
	}

	public static xbean.RoleDoubleChareInfo get(Long key, xbean.RoleDoubleChareInfo value) {
		return _Tables_.getInstance().roledoublecharge.get(key, value);
	}

	public static void insert(Long key, xbean.RoleDoubleChareInfo value) {
		_Tables_.getInstance().roledoublecharge.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roledoublecharge.delete(key);
	}

	public static boolean add(Long key, xbean.RoleDoubleChareInfo value) {
		return _Tables_.getInstance().roledoublecharge.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roledoublecharge.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleDoubleChareInfo> getCache() {
		return _Tables_.getInstance().roledoublecharge.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleDoubleChareInfo> getTable() {
		return _Tables_.getInstance().roledoublecharge;
	}

	public static xbean.RoleDoubleChareInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleDoubleChareInfo, xbean.RoleDoubleChareInfo>() {
			public xbean.RoleDoubleChareInfo get(xbean.RoleDoubleChareInfo v) { return v.toData(); }
		});
	}

	public static Long selectActivetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleDoubleChareInfo, Long>() {
				public Long get(xbean.RoleDoubleChareInfo v) { return v.getActivetime(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAchievement(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleDoubleChareInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.RoleDoubleChareInfo v) { return v.getAchievementAsData(); }
			});
	}

	public static Integer selectFlag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleDoubleChareInfo, Integer>() {
				public Integer get(xbean.RoleDoubleChareInfo v) { return v.getFlag(); }
			});
	}

}
