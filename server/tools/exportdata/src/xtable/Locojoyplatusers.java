package xtable;

// typed table access point
public class Locojoyplatusers {
	Locojoyplatusers() {
	}

	public static xbean.LocojoyPlatUserInfo get(String key) {
		return _Tables_.getInstance().locojoyplatusers.get(key);
	}

	public static xbean.LocojoyPlatUserInfo get(String key, xbean.LocojoyPlatUserInfo value) {
		return _Tables_.getInstance().locojoyplatusers.get(key, value);
	}

	public static void insert(String key, xbean.LocojoyPlatUserInfo value) {
		_Tables_.getInstance().locojoyplatusers.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().locojoyplatusers.delete(key);
	}

	public static boolean add(String key, xbean.LocojoyPlatUserInfo value) {
		return _Tables_.getInstance().locojoyplatusers.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().locojoyplatusers.remove(key);
	}

	public static xdb.TTableCache<String, xbean.LocojoyPlatUserInfo> getCache() {
		return _Tables_.getInstance().locojoyplatusers.getCache();
	}

	public static xdb.TTable<String, xbean.LocojoyPlatUserInfo> getTable() {
		return _Tables_.getInstance().locojoyplatusers;
	}

	public static xbean.LocojoyPlatUserInfo select(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatUserInfo, xbean.LocojoyPlatUserInfo>() {
			public xbean.LocojoyPlatUserInfo get(xbean.LocojoyPlatUserInfo v) { return v.toData(); }
		});
	}

	public static Integer selectUserid(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatUserInfo, Integer>() {
				public Integer get(xbean.LocojoyPlatUserInfo v) { return v.getUserid(); }
			});
	}

	public static Long selectUserinfoid(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatUserInfo, Long>() {
				public Long get(xbean.LocojoyPlatUserInfo v) { return v.getUserinfoid(); }
			});
	}

}
