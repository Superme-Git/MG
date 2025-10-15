package xtable;

// typed table access point
public class Requestruneinfotab {
	Requestruneinfotab() {
	}

	public static xbean.RequestRuneInfoList get(Long key) {
		return _Tables_.getInstance().requestruneinfotab.get(key);
	}

	public static xbean.RequestRuneInfoList get(Long key, xbean.RequestRuneInfoList value) {
		return _Tables_.getInstance().requestruneinfotab.get(key, value);
	}

	public static void insert(Long key, xbean.RequestRuneInfoList value) {
		_Tables_.getInstance().requestruneinfotab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().requestruneinfotab.delete(key);
	}

	public static boolean add(Long key, xbean.RequestRuneInfoList value) {
		return _Tables_.getInstance().requestruneinfotab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().requestruneinfotab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RequestRuneInfoList> getCache() {
		return _Tables_.getInstance().requestruneinfotab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RequestRuneInfoList> getTable() {
		return _Tables_.getInstance().requestruneinfotab;
	}

	public static xbean.RequestRuneInfoList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RequestRuneInfoList, xbean.RequestRuneInfoList>() {
			public xbean.RequestRuneInfoList get(xbean.RequestRuneInfoList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RequestRuneInfo> selectRequestrunelists(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RequestRuneInfoList, java.util.List<xbean.RequestRuneInfo>>() {
				public java.util.List<xbean.RequestRuneInfo> get(xbean.RequestRuneInfoList v) { return v.getRequestrunelistsAsData(); }
			});
	}

}
