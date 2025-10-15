package xtable;

// typed table access point
public class Roleshidelist {
	Roleshidelist() {
	}

	public static xbean.ShiDeZhiRankList get(Integer key) {
		return _Tables_.getInstance().roleshidelist.get(key);
	}

	public static xbean.ShiDeZhiRankList get(Integer key, xbean.ShiDeZhiRankList value) {
		return _Tables_.getInstance().roleshidelist.get(key, value);
	}

	public static void insert(Integer key, xbean.ShiDeZhiRankList value) {
		_Tables_.getInstance().roleshidelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().roleshidelist.delete(key);
	}

	public static boolean add(Integer key, xbean.ShiDeZhiRankList value) {
		return _Tables_.getInstance().roleshidelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().roleshidelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ShiDeZhiRankList> getCache() {
		return _Tables_.getInstance().roleshidelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ShiDeZhiRankList> getTable() {
		return _Tables_.getInstance().roleshidelist;
	}

	public static xbean.ShiDeZhiRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ShiDeZhiRankList, xbean.ShiDeZhiRankList>() {
			public xbean.ShiDeZhiRankList get(xbean.ShiDeZhiRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.MasterRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ShiDeZhiRankList, java.util.List<xbean.MasterRankRecord>>() {
				public java.util.List<xbean.MasterRankRecord> get(xbean.ShiDeZhiRankList v) { return v.getRecordsAsData(); }
			});
	}

}
