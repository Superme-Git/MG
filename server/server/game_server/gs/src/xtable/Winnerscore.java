package xtable;

// typed table access point
public class Winnerscore {
	Winnerscore() {
	}

	public static xbean.WinnerRoleRecord get(Long key) {
		return _Tables_.getInstance().winnerscore.get(key);
	}

	public static xbean.WinnerRoleRecord get(Long key, xbean.WinnerRoleRecord value) {
		return _Tables_.getInstance().winnerscore.get(key, value);
	}

	public static void insert(Long key, xbean.WinnerRoleRecord value) {
		_Tables_.getInstance().winnerscore.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().winnerscore.delete(key);
	}

	public static boolean add(Long key, xbean.WinnerRoleRecord value) {
		return _Tables_.getInstance().winnerscore.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().winnerscore.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.WinnerRoleRecord> getCache() {
		return _Tables_.getInstance().winnerscore.getCache();
	}

	public static mkdb.TTable<Long, xbean.WinnerRoleRecord> getTable() {
		return _Tables_.getInstance().winnerscore;
	}

	public static xbean.WinnerRoleRecord select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WinnerRoleRecord, xbean.WinnerRoleRecord>() {
			public xbean.WinnerRoleRecord get(xbean.WinnerRoleRecord v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WinnerRoleRecord, Long>() {
				public Long get(xbean.WinnerRoleRecord v) { return v.getRoleid(); }
			});
	}

	public static Integer selectScore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WinnerRoleRecord, Integer>() {
				public Integer get(xbean.WinnerRoleRecord v) { return v.getScore(); }
			});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WinnerRoleRecord, Long>() {
				public Long get(xbean.WinnerRoleRecord v) { return v.getTime(); }
			});
	}

	public static Integer selectAwardflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WinnerRoleRecord, Integer>() {
				public Integer get(xbean.WinnerRoleRecord v) { return v.getAwardflag(); }
			});
	}

}
