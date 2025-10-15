package xtable;

// typed table access point
public class Rolezonghelist {
	Rolezonghelist() {
	}

	public static xbean.RoleZongheRankList get(Integer key) {
		return _Tables_.getInstance().rolezonghelist.get(key);
	}

	public static xbean.RoleZongheRankList get(Integer key, xbean.RoleZongheRankList value) {
		return _Tables_.getInstance().rolezonghelist.get(key, value);
	}

	public static void insert(Integer key, xbean.RoleZongheRankList value) {
		_Tables_.getInstance().rolezonghelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().rolezonghelist.delete(key);
	}

	public static boolean add(Integer key, xbean.RoleZongheRankList value) {
		return _Tables_.getInstance().rolezonghelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().rolezonghelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RoleZongheRankList> getCache() {
		return _Tables_.getInstance().rolezonghelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RoleZongheRankList> getTable() {
		return _Tables_.getInstance().rolezonghelist;
	}

	public static xbean.RoleZongheRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleZongheRankList, xbean.RoleZongheRankList>() {
			public xbean.RoleZongheRankList get(xbean.RoleZongheRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RoleZongheRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleZongheRankList, java.util.List<xbean.RoleZongheRankRecord>>() {
				public java.util.List<xbean.RoleZongheRankRecord> get(xbean.RoleZongheRankList v) { return v.getRecordsAsData(); }
			});
	}

}
