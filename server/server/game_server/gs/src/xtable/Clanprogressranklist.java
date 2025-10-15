package xtable;

// typed table access point
public class Clanprogressranklist {
	Clanprogressranklist() {
	}

	public static xbean.ClanProgressRankList get(Integer key) {
		return _Tables_.getInstance().clanprogressranklist.get(key);
	}

	public static xbean.ClanProgressRankList get(Integer key, xbean.ClanProgressRankList value) {
		return _Tables_.getInstance().clanprogressranklist.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanProgressRankList value) {
		_Tables_.getInstance().clanprogressranklist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().clanprogressranklist.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanProgressRankList value) {
		return _Tables_.getInstance().clanprogressranklist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().clanprogressranklist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanProgressRankList> getCache() {
		return _Tables_.getInstance().clanprogressranklist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanProgressRankList> getTable() {
		return _Tables_.getInstance().clanprogressranklist;
	}

	public static xbean.ClanProgressRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanProgressRankList, xbean.ClanProgressRankList>() {
			public xbean.ClanProgressRankList get(xbean.ClanProgressRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ClanProgressRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanProgressRankList, java.util.List<xbean.ClanProgressRankRecord>>() {
				public java.util.List<xbean.ClanProgressRankRecord> get(xbean.ClanProgressRankList v) { return v.getRecordsAsData(); }
			});
	}

}
