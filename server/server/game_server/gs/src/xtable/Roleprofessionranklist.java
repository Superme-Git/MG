package xtable;

// typed table access point
public class Roleprofessionranklist {
	Roleprofessionranklist() {
	}

	public static xbean.RoleProfessionRankList get(Integer key) {
		return _Tables_.getInstance().roleprofessionranklist.get(key);
	}

	public static xbean.RoleProfessionRankList get(Integer key, xbean.RoleProfessionRankList value) {
		return _Tables_.getInstance().roleprofessionranklist.get(key, value);
	}

	public static void insert(Integer key, xbean.RoleProfessionRankList value) {
		_Tables_.getInstance().roleprofessionranklist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().roleprofessionranklist.delete(key);
	}

	public static boolean add(Integer key, xbean.RoleProfessionRankList value) {
		return _Tables_.getInstance().roleprofessionranklist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().roleprofessionranklist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RoleProfessionRankList> getCache() {
		return _Tables_.getInstance().roleprofessionranklist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RoleProfessionRankList> getTable() {
		return _Tables_.getInstance().roleprofessionranklist;
	}

	public static xbean.RoleProfessionRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleProfessionRankList, xbean.RoleProfessionRankList>() {
			public xbean.RoleProfessionRankList get(xbean.RoleProfessionRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RoleProfessionRankRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleProfessionRankList, java.util.List<xbean.RoleProfessionRankRecord>>() {
				public java.util.List<xbean.RoleProfessionRankRecord> get(xbean.RoleProfessionRankList v) { return v.getRecordsAsData(); }
			});
	}

}
