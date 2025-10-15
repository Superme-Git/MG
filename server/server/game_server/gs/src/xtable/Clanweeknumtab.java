package xtable;

// typed table access point
public class Clanweeknumtab {
	Clanweeknumtab() {
	}

	public static xbean.ClanWeekNum get(Long key) {
		return _Tables_.getInstance().clanweeknumtab.get(key);
	}

	public static xbean.ClanWeekNum get(Long key, xbean.ClanWeekNum value) {
		return _Tables_.getInstance().clanweeknumtab.get(key, value);
	}

	public static void insert(Long key, xbean.ClanWeekNum value) {
		_Tables_.getInstance().clanweeknumtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clanweeknumtab.delete(key);
	}

	public static boolean add(Long key, xbean.ClanWeekNum value) {
		return _Tables_.getInstance().clanweeknumtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clanweeknumtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanWeekNum> getCache() {
		return _Tables_.getInstance().clanweeknumtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanWeekNum> getTable() {
		return _Tables_.getInstance().clanweeknumtab;
	}

	public static xbean.ClanWeekNum select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanWeekNum, xbean.ClanWeekNum>() {
			public xbean.ClanWeekNum get(xbean.ClanWeekNum v) { return v.toData(); }
		});
	}

	public static Long selectWeeknum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanWeekNum, Long>() {
				public Long get(xbean.ClanWeekNum v) { return v.getWeeknum(); }
			});
	}

}
