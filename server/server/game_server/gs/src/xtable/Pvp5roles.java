package xtable;

// typed table access point
public class Pvp5roles {
	Pvp5roles() {
	}

	public static xbean.PvP5Role get(Long key) {
		return _Tables_.getInstance().pvp5roles.get(key);
	}

	public static xbean.PvP5Role get(Long key, xbean.PvP5Role value) {
		return _Tables_.getInstance().pvp5roles.get(key, value);
	}

	public static void insert(Long key, xbean.PvP5Role value) {
		_Tables_.getInstance().pvp5roles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().pvp5roles.delete(key);
	}

	public static boolean add(Long key, xbean.PvP5Role value) {
		return _Tables_.getInstance().pvp5roles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().pvp5roles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PvP5Role> getCache() {
		return _Tables_.getInstance().pvp5roles.getCache();
	}

	public static mkdb.TTable<Long, xbean.PvP5Role> getTable() {
		return _Tables_.getInstance().pvp5roles;
	}

	public static xbean.PvP5Role select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, xbean.PvP5Role>() {
			public xbean.PvP5Role get(xbean.PvP5Role v) { return v.toData(); }
		});
	}

	public static Integer selectGrade(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getGrade(); }
			});
	}

	public static Integer selectCamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getCamp(); }
			});
	}

	public static Integer selectExcellent(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getExcellent(); }
			});
	}

	public static Integer selectScore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getScore(); }
			});
	}

	public static Integer selectBattlenum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getBattlenum(); }
			});
	}

	public static Integer selectWinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getWinnum(); }
			});
	}

	public static Integer selectContinuewinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getContinuewinnum(); }
			});
	}

	public static Integer selectFirstwinaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getFirstwinaward(); }
			});
	}

	public static Integer selectFivebattleaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Integer>() {
				public Integer get(xbean.PvP5Role v) { return v.getFivebattleaward(); }
			});
	}

	public static Long selectLastbattletime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Long>() {
				public Long get(xbean.PvP5Role v) { return v.getLastbattletime(); }
			});
	}

	public static Long selectLastentertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Role, Long>() {
				public Long get(xbean.PvP5Role v) { return v.getLastentertime(); }
			});
	}

}
