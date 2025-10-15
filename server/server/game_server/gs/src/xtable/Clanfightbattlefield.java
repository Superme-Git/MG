package xtable;

// typed table access point
public class Clanfightbattlefield {
	Clanfightbattlefield() {
	}

	public static xbean.ClanFightBattleField get(Long key) {
		return _Tables_.getInstance().clanfightbattlefield.get(key);
	}

	public static xbean.ClanFightBattleField get(Long key, xbean.ClanFightBattleField value) {
		return _Tables_.getInstance().clanfightbattlefield.get(key, value);
	}

	public static void insert(Long key, xbean.ClanFightBattleField value) {
		_Tables_.getInstance().clanfightbattlefield.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clanfightbattlefield.delete(key);
	}

	public static boolean add(Long key, xbean.ClanFightBattleField value) {
		return _Tables_.getInstance().clanfightbattlefield.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clanfightbattlefield.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanFightBattleField> getCache() {
		return _Tables_.getInstance().clanfightbattlefield.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanFightBattleField> getTable() {
		return _Tables_.getInstance().clanfightbattlefield;
	}

	public static xbean.ClanFightBattleField select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, xbean.ClanFightBattleField>() {
			public xbean.ClanFightBattleField get(xbean.ClanFightBattleField v) { return v.toData(); }
		});
	}

	public static Integer selectBattlefieldid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Integer>() {
				public Integer get(xbean.ClanFightBattleField v) { return v.getBattlefieldid(); }
			});
	}

	public static Long selectClanfightid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getClanfightid(); }
			});
	}

	public static Integer selectState(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Integer>() {
				public Integer get(xbean.ClanFightBattleField v) { return v.getState(); }
			});
	}

	public static Long selectClanid1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getClanid1(); }
			});
	}

	public static Long selectClanid2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getClanid2(); }
			});
	}

	public static String selectClanname1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, String>() {
				public String get(xbean.ClanFightBattleField v) { return v.getClanname1(); }
			});
	}

	public static String selectClanname2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, String>() {
				public String get(xbean.ClanFightBattleField v) { return v.getClanname2(); }
			});
	}

	public static Integer selectClanscore1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Integer>() {
				public Integer get(xbean.ClanFightBattleField v) { return v.getClanscore1(); }
			});
	}

	public static Integer selectClanscore2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Integer>() {
				public Integer get(xbean.ClanFightBattleField v) { return v.getClanscore2(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan1roleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan1roleidsAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan2roleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan2roleidsAsData(); }
			});
	}

	public static java.util.Map<Long, String> selectEnterroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, String>>() {
				public java.util.Map<Long, String> get(xbean.ClanFightBattleField v) { return v.getEnterroleidsAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan1enterroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan1enterroleidsAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan2enterroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan2enterroleidsAsData(); }
			});
	}

	public static java.util.List<xbean.BattleFieldScroeRank> selectClan1roleranklist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.List<xbean.BattleFieldScroeRank>>() {
				public java.util.List<xbean.BattleFieldScroeRank> get(xbean.ClanFightBattleField v) { return v.getClan1roleranklistAsData(); }
			});
	}

	public static java.util.List<xbean.BattleFieldScroeRank> selectClan2roleranklist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.List<xbean.BattleFieldScroeRank>>() {
				public java.util.List<xbean.BattleFieldScroeRank> get(xbean.ClanFightBattleField v) { return v.getClan2roleranklistAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan1rolerankmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan1rolerankmapAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectClan2rolerankmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.ClanFightBattleField v) { return v.getClan2rolerankmapAsData(); }
			});
	}

	public static Long selectLastclan1rolesortstamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getLastclan1rolesortstamp(); }
			});
	}

	public static Long selectLastclan2rolesortstamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getLastclan2rolesortstamp(); }
			});
	}

	public static java.util.List<Long> selectVictoryboxids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ClanFightBattleField v) { return v.getVictoryboxidsAsData(); }
			});
	}

	public static java.util.List<Long> selectCelebrateboxids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ClanFightBattleField v) { return v.getCelebrateboxidsAsData(); }
			});
	}

	public static Long selectDatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getDatetime(); }
			});
	}

	public static Long selectActivitystarttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getActivitystarttime(); }
			});
	}

	public static Long selectActivitypreparetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getActivitypreparetime(); }
			});
	}

	public static Long selectActivitynoentertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getActivitynoentertime(); }
			});
	}

	public static Long selectActivityendtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Long>() {
				public Long get(xbean.ClanFightBattleField v) { return v.getActivityendtime(); }
			});
	}

	public static Integer selectWhichwin(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightBattleField, Integer>() {
				public Integer get(xbean.ClanFightBattleField v) { return v.getWhichwin(); }
			});
	}

}
