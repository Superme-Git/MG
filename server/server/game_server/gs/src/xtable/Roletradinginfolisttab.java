package xtable;

// typed table access point
public class Roletradinginfolisttab {
	Roletradinginfolisttab() {
	}

	public static xbean.RoleTradingInfoList get(Long key) {
		return _Tables_.getInstance().roletradinginfolisttab.get(key);
	}

	public static xbean.RoleTradingInfoList get(Long key, xbean.RoleTradingInfoList value) {
		return _Tables_.getInstance().roletradinginfolisttab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleTradingInfoList value) {
		_Tables_.getInstance().roletradinginfolisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roletradinginfolisttab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleTradingInfoList value) {
		return _Tables_.getInstance().roletradinginfolisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roletradinginfolisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleTradingInfoList> getCache() {
		return _Tables_.getInstance().roletradinginfolisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleTradingInfoList> getTable() {
		return _Tables_.getInstance().roletradinginfolisttab;
	}

	public static xbean.RoleTradingInfoList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingInfoList, xbean.RoleTradingInfoList>() {
			public xbean.RoleTradingInfoList get(xbean.RoleTradingInfoList v) { return v.toData(); }
		});
	}

	public static java.util.Map<String, String> selectTradinglist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingInfoList, java.util.Map<String, String>>() {
				public java.util.Map<String, String> get(xbean.RoleTradingInfoList v) { return v.getTradinglistAsData(); }
			});
	}

}
