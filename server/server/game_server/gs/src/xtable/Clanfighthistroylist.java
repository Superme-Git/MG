package xtable;

// typed table access point
public class Clanfighthistroylist {
	Clanfighthistroylist() {
	}

	public static xbean.ClanFightHistroyRankList get(Integer key) {
		return _Tables_.getInstance().clanfighthistroylist.get(key);
	}

	public static xbean.ClanFightHistroyRankList get(Integer key, xbean.ClanFightHistroyRankList value) {
		return _Tables_.getInstance().clanfighthistroylist.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanFightHistroyRankList value) {
		_Tables_.getInstance().clanfighthistroylist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().clanfighthistroylist.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanFightHistroyRankList value) {
		return _Tables_.getInstance().clanfighthistroylist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().clanfighthistroylist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanFightHistroyRankList> getCache() {
		return _Tables_.getInstance().clanfighthistroylist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanFightHistroyRankList> getTable() {
		return _Tables_.getInstance().clanfighthistroylist;
	}

	public static xbean.ClanFightHistroyRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightHistroyRankList, xbean.ClanFightHistroyRankList>() {
			public xbean.ClanFightHistroyRankList get(xbean.ClanFightHistroyRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ClanFightHistroyRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFightHistroyRankList, java.util.List<xbean.ClanFightHistroyRankRecord>>() {
				public java.util.List<xbean.ClanFightHistroyRankRecord> get(xbean.ClanFightHistroyRankList v) { return v.getRecordsAsData(); }
			});
	}

}
