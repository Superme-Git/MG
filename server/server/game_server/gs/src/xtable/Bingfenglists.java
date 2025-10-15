package xtable;

// typed table access point
public class Bingfenglists {
	Bingfenglists() {
	}

	public static xbean.BingFengRank get(Integer key) {
		return _Tables_.getInstance().bingfenglists.get(key);
	}

	public static xbean.BingFengRank get(Integer key, xbean.BingFengRank value) {
		return _Tables_.getInstance().bingfenglists.get(key, value);
	}

	public static void insert(Integer key, xbean.BingFengRank value) {
		_Tables_.getInstance().bingfenglists.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().bingfenglists.delete(key);
	}

	public static boolean add(Integer key, xbean.BingFengRank value) {
		return _Tables_.getInstance().bingfenglists.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().bingfenglists.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.BingFengRank> getCache() {
		return _Tables_.getInstance().bingfenglists.getCache();
	}

	public static mkdb.TTable<Integer, xbean.BingFengRank> getTable() {
		return _Tables_.getInstance().bingfenglists;
	}

	public static xbean.BingFengRank select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRank, xbean.BingFengRank>() {
			public xbean.BingFengRank get(xbean.BingFengRank v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.BingFengRankList> selectRanks(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengRank, java.util.Map<Integer, xbean.BingFengRankList>>() {
				public java.util.Map<Integer, xbean.BingFengRankList> get(xbean.BingFengRank v) { return v.getRanksAsData(); }
			});
	}

}
