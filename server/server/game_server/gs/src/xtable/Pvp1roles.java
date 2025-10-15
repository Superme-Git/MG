package xtable;

// typed table access point
public class Pvp1roles {
	Pvp1roles() {
	}

	public static xbean.PvP1Role get(Long key) {
		return _Tables_.getInstance().pvp1roles.get(key);
	}

	public static xbean.PvP1Role get(Long key, xbean.PvP1Role value) {
		return _Tables_.getInstance().pvp1roles.get(key, value);
	}

	public static void insert(Long key, xbean.PvP1Role value) {
		_Tables_.getInstance().pvp1roles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().pvp1roles.delete(key);
	}

	public static boolean add(Long key, xbean.PvP1Role value) {
		return _Tables_.getInstance().pvp1roles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().pvp1roles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PvP1Role> getCache() {
		return _Tables_.getInstance().pvp1roles.getCache();
	}

	public static mkdb.TTable<Long, xbean.PvP1Role> getTable() {
		return _Tables_.getInstance().pvp1roles;
	}

	public static xbean.PvP1Role select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, xbean.PvP1Role>() {
			public xbean.PvP1Role get(xbean.PvP1Role v) { return v.toData(); }
		});
	}

	public static Integer selectGrade(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getGrade(); }
			});
	}

	public static Integer selectScore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getScore(); }
			});
	}

	public static Integer selectBattlenum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getBattlenum(); }
			});
	}

	public static Integer selectWinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getWinnum(); }
			});
	}

	public static Integer selectContinuewinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getContinuewinnum(); }
			});
	}

	public static Integer selectFirstwinaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getFirstwinaward(); }
			});
	}

	public static Integer selectFivebattleaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Integer>() {
				public Integer get(xbean.PvP1Role v) { return v.getFivebattleaward(); }
			});
	}

	public static Long selectLastbattletime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Long>() {
				public Long get(xbean.PvP1Role v) { return v.getLastbattletime(); }
			});
	}

	public static Long selectLastentertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Role, Long>() {
				public Long get(xbean.PvP1Role v) { return v.getLastentertime(); }
			});
	}

}
