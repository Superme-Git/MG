package xtable;

// typed table access point
public class Clanzonghelist {
	Clanzonghelist() {
	}

	public static xbean.ClanZongHeRankList get(Integer key) {
		return _Tables_.getInstance().clanzonghelist.get(key);
	}

	public static xbean.ClanZongHeRankList get(Integer key, xbean.ClanZongHeRankList value) {
		return _Tables_.getInstance().clanzonghelist.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanZongHeRankList value) {
		_Tables_.getInstance().clanzonghelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().clanzonghelist.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanZongHeRankList value) {
		return _Tables_.getInstance().clanzonghelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().clanzonghelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanZongHeRankList> getCache() {
		return _Tables_.getInstance().clanzonghelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanZongHeRankList> getTable() {
		return _Tables_.getInstance().clanzonghelist;
	}

	public static xbean.ClanZongHeRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanZongHeRankList, xbean.ClanZongHeRankList>() {
			public xbean.ClanZongHeRankList get(xbean.ClanZongHeRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ClanZongHeRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanZongHeRankList, java.util.List<xbean.ClanZongHeRankRecord>>() {
				public java.util.List<xbean.ClanZongHeRankRecord> get(xbean.ClanZongHeRankList v) { return v.getRecordsAsData(); }
			});
	}

}
