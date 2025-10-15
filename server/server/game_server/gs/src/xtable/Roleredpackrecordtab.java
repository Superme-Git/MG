package xtable;

// typed table access point
public class Roleredpackrecordtab {
	Roleredpackrecordtab() {
	}

	public static xbean.RoleRedPackRecord get(Long key) {
		return _Tables_.getInstance().roleredpackrecordtab.get(key);
	}

	public static xbean.RoleRedPackRecord get(Long key, xbean.RoleRedPackRecord value) {
		return _Tables_.getInstance().roleredpackrecordtab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleRedPackRecord value) {
		_Tables_.getInstance().roleredpackrecordtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleredpackrecordtab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleRedPackRecord value) {
		return _Tables_.getInstance().roleredpackrecordtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleredpackrecordtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleRedPackRecord> getCache() {
		return _Tables_.getInstance().roleredpackrecordtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleRedPackRecord> getTable() {
		return _Tables_.getInstance().roleredpackrecordtab;
	}

	public static xbean.RoleRedPackRecord select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, xbean.RoleRedPackRecord>() {
			public xbean.RoleRedPackRecord get(xbean.RoleRedPackRecord v) { return v.toData(); }
		});
	}

	public static Long selectSendnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getSendnum(); }
			});
	}

	public static Long selectSendgold(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getSendgold(); }
			});
	}

	public static Long selectSendfushi(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getSendfushi(); }
			});
	}

	public static Long selectReceivenum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getReceivenum(); }
			});
	}

	public static Long selectReceivegold(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getReceivegold(); }
			});
	}

	public static Long selectReceivefushi(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRedPackRecord, Long>() {
				public Long get(xbean.RoleRedPackRecord v) { return v.getReceivefushi(); }
			});
	}

}
