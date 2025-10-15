package xtable;

// typed table access point
public class Redpacklist {
	Redpacklist() {
	}

	public static xbean.RedPackRankList get(Integer key) {
		return _Tables_.getInstance().redpacklist.get(key);
	}

	public static xbean.RedPackRankList get(Integer key, xbean.RedPackRankList value) {
		return _Tables_.getInstance().redpacklist.get(key, value);
	}

	public static void insert(Integer key, xbean.RedPackRankList value) {
		_Tables_.getInstance().redpacklist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().redpacklist.delete(key);
	}

	public static boolean add(Integer key, xbean.RedPackRankList value) {
		return _Tables_.getInstance().redpacklist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().redpacklist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RedPackRankList> getCache() {
		return _Tables_.getInstance().redpacklist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RedPackRankList> getTable() {
		return _Tables_.getInstance().redpacklist;
	}

	public static xbean.RedPackRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackRankList, xbean.RedPackRankList>() {
			public xbean.RedPackRankList get(xbean.RedPackRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RedPackRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackRankList, java.util.List<xbean.RedPackRecord>>() {
				public java.util.List<xbean.RedPackRecord> get(xbean.RedPackRankList v) { return v.getRecordsAsData(); }
			});
	}

}
