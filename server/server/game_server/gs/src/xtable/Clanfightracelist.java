package xtable;

// typed table access point
public class Clanfightracelist {
	Clanfightracelist() {
	}

	public static xbean.ClanFightRaceRankList get(Long key) {
		return _Tables_.getInstance().clanfightracelist.get(key);
	}

	public static xbean.ClanFightRaceRankList get(Long key, xbean.ClanFightRaceRankList value) {
		return _Tables_.getInstance().clanfightracelist.get(key, value);
	}

	public static void insert(Long key, xbean.ClanFightRaceRankList value) {
		_Tables_.getInstance().clanfightracelist.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clanfightracelist.delete(key);
	}

	public static boolean add(Long key, xbean.ClanFightRaceRankList value) {
		return _Tables_.getInstance().clanfightracelist.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clanfightracelist.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanFightRaceRankList> getCache() {
		return _Tables_.getInstance().clanfightracelist.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanFightRaceRankList> getTable() {
		return _Tables_.getInstance().clanfightracelist;
	}

	public static xbean.ClanFightRaceRankList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightRaceRankList, xbean.ClanFightRaceRankList>() {
			public xbean.ClanFightRaceRankList get(xbean.ClanFightRaceRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ClanFightRaceRankRecord> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightRaceRankList, java.util.List<xbean.ClanFightRaceRankRecord>>() {
				public java.util.List<xbean.ClanFightRaceRankRecord> get(xbean.ClanFightRaceRankList v) { return v.getRecordsAsData(); }
			});
	}

}
