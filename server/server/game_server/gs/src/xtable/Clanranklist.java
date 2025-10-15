package xtable;

// typed table access point
public class Clanranklist {
	Clanranklist() {
	}

	public static xbean.ClanRankList get(Integer key) {
		return _Tables_.getInstance().clanranklist.get(key);
	}

	public static xbean.ClanRankList get(Integer key, xbean.ClanRankList value) {
		return _Tables_.getInstance().clanranklist.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanRankList value) {
		_Tables_.getInstance().clanranklist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().clanranklist.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanRankList value) {
		return _Tables_.getInstance().clanranklist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().clanranklist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanRankList> getCache() {
		return _Tables_.getInstance().clanranklist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanRankList> getTable() {
		return _Tables_.getInstance().clanranklist;
	}

	public static xbean.ClanRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanRankList, xbean.ClanRankList>() {
			public xbean.ClanRankList get(xbean.ClanRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ClanRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanRankList, java.util.List<xbean.ClanRankRecord>>() {
				public java.util.List<xbean.ClanRankRecord> get(xbean.ClanRankList v) { return v.getRecordsAsData(); }
			});
	}

}
