package xtable;

// typed table access point
public class Rolelevellist {
	Rolelevellist() {
	}

	public static xbean.RoleLevelRankList get(Integer key) {
		return _Tables_.getInstance().rolelevellist.get(key);
	}

	public static xbean.RoleLevelRankList get(Integer key, xbean.RoleLevelRankList value) {
		return _Tables_.getInstance().rolelevellist.get(key, value);
	}

	public static void insert(Integer key, xbean.RoleLevelRankList value) {
		_Tables_.getInstance().rolelevellist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().rolelevellist.delete(key);
	}

	public static boolean add(Integer key, xbean.RoleLevelRankList value) {
		return _Tables_.getInstance().rolelevellist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().rolelevellist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RoleLevelRankList> getCache() {
		return _Tables_.getInstance().rolelevellist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RoleLevelRankList> getTable() {
		return _Tables_.getInstance().rolelevellist;
	}

	public static xbean.RoleLevelRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLevelRankList, xbean.RoleLevelRankList>() {
			public xbean.RoleLevelRankList get(xbean.RoleLevelRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RoleLevelListRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLevelRankList, java.util.List<xbean.RoleLevelListRecord>>() {
				public java.util.List<xbean.RoleLevelListRecord> get(xbean.RoleLevelRankList v) { return v.getRecordsAsData(); }
			});
	}

}
