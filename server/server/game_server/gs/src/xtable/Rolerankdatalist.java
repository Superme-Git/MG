package xtable;

// typed table access point
public class Rolerankdatalist {
	Rolerankdatalist() {
	}

	public static xbean.RoleRankList get(Integer key) {
		return _Tables_.getInstance().rolerankdatalist.get(key);
	}

	public static xbean.RoleRankList get(Integer key, xbean.RoleRankList value) {
		return _Tables_.getInstance().rolerankdatalist.get(key, value);
	}

	public static void insert(Integer key, xbean.RoleRankList value) {
		_Tables_.getInstance().rolerankdatalist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().rolerankdatalist.delete(key);
	}

	public static boolean add(Integer key, xbean.RoleRankList value) {
		return _Tables_.getInstance().rolerankdatalist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().rolerankdatalist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RoleRankList> getCache() {
		return _Tables_.getInstance().rolerankdatalist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RoleRankList> getTable() {
		return _Tables_.getInstance().rolerankdatalist;
	}

	public static xbean.RoleRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRankList, xbean.RoleRankList>() {
			public xbean.RoleRankList get(xbean.RoleRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RoleRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRankList, java.util.List<xbean.RoleRankRecord>>() {
				public java.util.List<xbean.RoleRankRecord> get(xbean.RoleRankList v) { return v.getRecordsAsData(); }
			});
	}

}
