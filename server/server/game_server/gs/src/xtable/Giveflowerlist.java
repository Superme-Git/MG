package xtable;

// typed table access point
public class Giveflowerlist {
	Giveflowerlist() {
	}

	public static xbean.GiveFlowerRankList get(Long key) {
		return _Tables_.getInstance().giveflowerlist.get(key);
	}

	public static xbean.GiveFlowerRankList get(Long key, xbean.GiveFlowerRankList value) {
		return _Tables_.getInstance().giveflowerlist.get(key, value);
	}

	public static void insert(Long key, xbean.GiveFlowerRankList value) {
		_Tables_.getInstance().giveflowerlist.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().giveflowerlist.delete(key);
	}

	public static boolean add(Long key, xbean.GiveFlowerRankList value) {
		return _Tables_.getInstance().giveflowerlist.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().giveflowerlist.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.GiveFlowerRankList> getCache() {
		return _Tables_.getInstance().giveflowerlist.getCache();
	}

	public static mkdb.TTable<Long, xbean.GiveFlowerRankList> getTable() {
		return _Tables_.getInstance().giveflowerlist;
	}

	public static xbean.GiveFlowerRankList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.GiveFlowerRankList, xbean.GiveFlowerRankList>() {
			public xbean.GiveFlowerRankList get(xbean.GiveFlowerRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.GiveFlowerRecord> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.GiveFlowerRankList, java.util.List<xbean.GiveFlowerRecord>>() {
				public java.util.List<xbean.GiveFlowerRecord> get(xbean.GiveFlowerRankList v) { return v.getRecordsAsData(); }
			});
	}

}
