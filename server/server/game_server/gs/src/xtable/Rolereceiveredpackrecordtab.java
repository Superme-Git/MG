package xtable;

// typed table access point
public class Rolereceiveredpackrecordtab {
	Rolereceiveredpackrecordtab() {
	}

	public static xbean.RoleReceiveRedPackRecordList get(Long key) {
		return _Tables_.getInstance().rolereceiveredpackrecordtab.get(key);
	}

	public static xbean.RoleReceiveRedPackRecordList get(Long key, xbean.RoleReceiveRedPackRecordList value) {
		return _Tables_.getInstance().rolereceiveredpackrecordtab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleReceiveRedPackRecordList value) {
		_Tables_.getInstance().rolereceiveredpackrecordtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolereceiveredpackrecordtab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleReceiveRedPackRecordList value) {
		return _Tables_.getInstance().rolereceiveredpackrecordtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolereceiveredpackrecordtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleReceiveRedPackRecordList> getCache() {
		return _Tables_.getInstance().rolereceiveredpackrecordtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleReceiveRedPackRecordList> getTable() {
		return _Tables_.getInstance().rolereceiveredpackrecordtab;
	}

	public static xbean.RoleReceiveRedPackRecordList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleReceiveRedPackRecordList, xbean.RoleReceiveRedPackRecordList>() {
			public xbean.RoleReceiveRedPackRecordList get(xbean.RoleReceiveRedPackRecordList v) { return v.toData(); }
		});
	}

	public static java.util.Map<String, xbean.RoleReceiveRedPackRecord> selectRolereceiveredpacklist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleReceiveRedPackRecordList, java.util.Map<String, xbean.RoleReceiveRedPackRecord>>() {
				public java.util.Map<String, xbean.RoleReceiveRedPackRecord> get(xbean.RoleReceiveRedPackRecordList v) { return v.getRolereceiveredpacklistAsData(); }
			});
	}

}
