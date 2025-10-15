package xtable;

// typed table access point
public class Receflowerlist {
	Receflowerlist() {
	}

	public static xbean.ReceFlowerRankList get(Long key) {
		return _Tables_.getInstance().receflowerlist.get(key);
	}

	public static xbean.ReceFlowerRankList get(Long key, xbean.ReceFlowerRankList value) {
		return _Tables_.getInstance().receflowerlist.get(key, value);
	}

	public static void insert(Long key, xbean.ReceFlowerRankList value) {
		_Tables_.getInstance().receflowerlist.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().receflowerlist.delete(key);
	}

	public static boolean add(Long key, xbean.ReceFlowerRankList value) {
		return _Tables_.getInstance().receflowerlist.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().receflowerlist.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ReceFlowerRankList> getCache() {
		return _Tables_.getInstance().receflowerlist.getCache();
	}

	public static mkdb.TTable<Long, xbean.ReceFlowerRankList> getTable() {
		return _Tables_.getInstance().receflowerlist;
	}

	public static xbean.ReceFlowerRankList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ReceFlowerRankList, xbean.ReceFlowerRankList>() {
			public xbean.ReceFlowerRankList get(xbean.ReceFlowerRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ReceFlowerRecord> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ReceFlowerRankList, java.util.List<xbean.ReceFlowerRecord>>() {
				public java.util.List<xbean.ReceFlowerRecord> get(xbean.ReceFlowerRankList v) { return v.getRecordsAsData(); }
			});
	}

}
