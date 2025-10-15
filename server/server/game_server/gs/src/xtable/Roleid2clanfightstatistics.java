package xtable;

// typed table access point
public class Roleid2clanfightstatistics {
	Roleid2clanfightstatistics() {
	}

	public static xbean.EClanFightStatistics get(Long key) {
		return _Tables_.getInstance().roleid2clanfightstatistics.get(key);
	}

	public static xbean.EClanFightStatistics get(Long key, xbean.EClanFightStatistics value) {
		return _Tables_.getInstance().roleid2clanfightstatistics.get(key, value);
	}

	public static void insert(Long key, xbean.EClanFightStatistics value) {
		_Tables_.getInstance().roleid2clanfightstatistics.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2clanfightstatistics.delete(key);
	}

	public static boolean add(Long key, xbean.EClanFightStatistics value) {
		return _Tables_.getInstance().roleid2clanfightstatistics.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2clanfightstatistics.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EClanFightStatistics> getCache() {
		return _Tables_.getInstance().roleid2clanfightstatistics.getCache();
	}

	public static mkdb.TTable<Long, xbean.EClanFightStatistics> getTable() {
		return _Tables_.getInstance().roleid2clanfightstatistics;
	}

	public static xbean.EClanFightStatistics select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, xbean.EClanFightStatistics>() {
			public xbean.EClanFightStatistics get(xbean.EClanFightStatistics v) { return v.toData(); }
		});
	}

	public static Integer selectAct(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Integer>() {
				public Integer get(xbean.EClanFightStatistics v) { return v.getAct(); }
			});
	}

	public static Integer selectScore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Integer>() {
				public Integer get(xbean.EClanFightStatistics v) { return v.getScore(); }
			});
	}

	public static Long selectEntertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Long>() {
				public Long get(xbean.EClanFightStatistics v) { return v.getEntertime(); }
			});
	}

	public static Integer selectWinneritemnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Integer>() {
				public Integer get(xbean.EClanFightStatistics v) { return v.getWinneritemnum(); }
			});
	}

	public static Integer selectClancelebrateitemnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Integer>() {
				public Integer get(xbean.EClanFightStatistics v) { return v.getClancelebrateitemnum(); }
			});
	}

	public static Integer selectWinnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Integer>() {
				public Integer get(xbean.EClanFightStatistics v) { return v.getWinnum(); }
			});
	}

	public static Long selectLastcbattlefieldrankliststamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Long>() {
				public Long get(xbean.EClanFightStatistics v) { return v.getLastcbattlefieldrankliststamp(); }
			});
	}

	public static Long selectLastlosestamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanFightStatistics, Long>() {
				public Long get(xbean.EClanFightStatistics v) { return v.getLastlosestamp(); }
			});
	}

}
