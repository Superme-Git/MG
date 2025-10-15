package xtable;

// typed table access point
public class Locojoyplatorderinfos {
	Locojoyplatorderinfos() {
	}

	public static xbean.LocojoyPlatOrderInfo get(String key) {
		return _Tables_.getInstance().locojoyplatorderinfos.get(key);
	}

	public static xbean.LocojoyPlatOrderInfo get(String key, xbean.LocojoyPlatOrderInfo value) {
		return _Tables_.getInstance().locojoyplatorderinfos.get(key, value);
	}

	public static void insert(String key, xbean.LocojoyPlatOrderInfo value) {
		_Tables_.getInstance().locojoyplatorderinfos.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().locojoyplatorderinfos.delete(key);
	}

	public static boolean add(String key, xbean.LocojoyPlatOrderInfo value) {
		return _Tables_.getInstance().locojoyplatorderinfos.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().locojoyplatorderinfos.remove(key);
	}

	public static xdb.TTableCache<String, xbean.LocojoyPlatOrderInfo> getCache() {
		return _Tables_.getInstance().locojoyplatorderinfos.getCache();
	}

	public static xdb.TTable<String, xbean.LocojoyPlatOrderInfo> getTable() {
		return _Tables_.getInstance().locojoyplatorderinfos;
	}

	public static xbean.LocojoyPlatOrderInfo select(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, xbean.LocojoyPlatOrderInfo>() {
			public xbean.LocojoyPlatOrderInfo get(xbean.LocojoyPlatOrderInfo v) { return v.toData(); }
		});
	}

	public static Long selectCreatetime(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, Long>() {
				public Long get(xbean.LocojoyPlatOrderInfo v) { return v.getCreatetime(); }
			});
	}

	public static String selectOrderserialplat(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, String>() {
				public String get(xbean.LocojoyPlatOrderInfo v) { return v.getOrderserialplat(); }
			});
	}

	public static String selectOrderserialgame(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, String>() {
				public String get(xbean.LocojoyPlatOrderInfo v) { return v.getOrderserialgame(); }
			});
	}

	public static String selectUsername(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, String>() {
				public String get(xbean.LocojoyPlatOrderInfo v) { return v.getUsername(); }
			});
	}

	public static byte [] selectVars(String key) {
		return getTable().select(key, new xdb.TField<xbean.LocojoyPlatOrderInfo, byte []>() {
				public byte [] get(xbean.LocojoyPlatOrderInfo v) { return v.getVarsCopy(); }
			});
	}

}
