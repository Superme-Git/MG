package xtable;

// typed table access point
public class Pvp3roles {
	Pvp3roles() {
	}

	public static xbean.PvP3Role get(Long key) {
		return _Tables_.getInstance().pvp3roles.get(key);
	}

	public static xbean.PvP3Role get(Long key, xbean.PvP3Role value) {
		return _Tables_.getInstance().pvp3roles.get(key, value);
	}

	public static void insert(Long key, xbean.PvP3Role value) {
		_Tables_.getInstance().pvp3roles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().pvp3roles.delete(key);
	}

	public static boolean add(Long key, xbean.PvP3Role value) {
		return _Tables_.getInstance().pvp3roles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().pvp3roles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PvP3Role> getCache() {
		return _Tables_.getInstance().pvp3roles.getCache();
	}

	public static mkdb.TTable<Long, xbean.PvP3Role> getTable() {
		return _Tables_.getInstance().pvp3roles;
	}

	public static xbean.PvP3Role select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, xbean.PvP3Role>() {
			public xbean.PvP3Role get(xbean.PvP3Role v) { return v.toData(); }
		});
	}

	public static Integer selectGrade(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getGrade(); }
			});
	}

	public static Integer selectScore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getScore(); }
			});
	}

	public static Integer selectBattlenum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getBattlenum(); }
			});
	}

	public static Integer selectWinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getWinnum(); }
			});
	}

	public static Integer selectContinuewinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getContinuewinnum(); }
			});
	}

	public static Integer selectFirstwinaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getFirstwinaward(); }
			});
	}

	public static Integer selectFivewinaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getFivewinaward(); }
			});
	}

	public static Integer selectTenbattleaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Integer>() {
				public Integer get(xbean.PvP3Role v) { return v.getTenbattleaward(); }
			});
	}

	public static Long selectLastbattletime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Long>() {
				public Long get(xbean.PvP3Role v) { return v.getLastbattletime(); }
			});
	}

	public static Long selectLastentertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Role, Long>() {
				public Long get(xbean.PvP3Role v) { return v.getLastentertime(); }
			});
	}

}
