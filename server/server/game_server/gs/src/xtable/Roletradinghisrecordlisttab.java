package xtable;

// typed table access point
public class Roletradinghisrecordlisttab {
	Roletradinghisrecordlisttab() {
	}

	public static xbean.RoleTradingHisRecordList get(Long key) {
		return _Tables_.getInstance().roletradinghisrecordlisttab.get(key);
	}

	public static xbean.RoleTradingHisRecordList get(Long key, xbean.RoleTradingHisRecordList value) {
		return _Tables_.getInstance().roletradinghisrecordlisttab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleTradingHisRecordList value) {
		_Tables_.getInstance().roletradinghisrecordlisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roletradinghisrecordlisttab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleTradingHisRecordList value) {
		return _Tables_.getInstance().roletradinghisrecordlisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roletradinghisrecordlisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleTradingHisRecordList> getCache() {
		return _Tables_.getInstance().roletradinghisrecordlisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleTradingHisRecordList> getTable() {
		return _Tables_.getInstance().roletradinghisrecordlisttab;
	}

	public static xbean.RoleTradingHisRecordList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingHisRecordList, xbean.RoleTradingHisRecordList>() {
			public xbean.RoleTradingHisRecordList get(xbean.RoleTradingHisRecordList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.RoleTradingHisRecord> selectRoletradinghisrecordlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleTradingHisRecordList, java.util.List<xbean.RoleTradingHisRecord>>() {
				public java.util.List<xbean.RoleTradingHisRecord> get(xbean.RoleTradingHisRecordList v) { return v.getRoletradinghisrecordlistAsData(); }
			});
	}

}
