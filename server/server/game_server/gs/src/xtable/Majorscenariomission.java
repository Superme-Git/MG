package xtable;

// typed table access point
public class Majorscenariomission {
	Majorscenariomission() {
	}

	public static xbean.Mission get(Long key) {
		return _Tables_.getInstance().majorscenariomission.get(key);
	}

	public static xbean.Mission get(Long key, xbean.Mission value) {
		return _Tables_.getInstance().majorscenariomission.get(key, value);
	}

	public static void insert(Long key, xbean.Mission value) {
		_Tables_.getInstance().majorscenariomission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().majorscenariomission.delete(key);
	}

	public static boolean add(Long key, xbean.Mission value) {
		return _Tables_.getInstance().majorscenariomission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().majorscenariomission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Mission> getCache() {
		return _Tables_.getInstance().majorscenariomission.getCache();
	}

	public static mkdb.TTable<Long, xbean.Mission> getTable() {
		return _Tables_.getInstance().majorscenariomission;
	}

	public static xbean.Mission select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, xbean.Mission>() {
			public xbean.Mission get(xbean.Mission v) { return v.toData(); }
		});
	}

	public static Integer selectId(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, Integer>() {
				public Integer get(xbean.Mission v) { return v.getId(); }
			});
	}

	public static Integer selectStatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, Integer>() {
				public Integer get(xbean.Mission v) { return v.getStatus(); }
			});
	}

	public static java.util.List<Integer> selectPath(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.Mission v) { return v.getPathAsData(); }
			});
	}

	public static Integer selectTaskvalue(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, Integer>() {
				public Integer get(xbean.Mission v) { return v.getTaskvalue(); }
			});
	}

	public static Long selectDeadline(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, Long>() {
				public Long get(xbean.Mission v) { return v.getDeadline(); }
			});
	}

	public static Integer selectRound(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Mission, Integer>() {
				public Integer get(xbean.Mission v) { return v.getRound(); }
			});
	}

}
