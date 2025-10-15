package xtable;

// typed table access point
public class Userdeviceinfotab {
	Userdeviceinfotab() {
	}

	public static xbean.UserDeviceInfo get(Integer key) {
		return _Tables_.getInstance().userdeviceinfotab.get(key);
	}

	public static xbean.UserDeviceInfo get(Integer key, xbean.UserDeviceInfo value) {
		return _Tables_.getInstance().userdeviceinfotab.get(key, value);
	}

	public static void insert(Integer key, xbean.UserDeviceInfo value) {
		_Tables_.getInstance().userdeviceinfotab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().userdeviceinfotab.delete(key);
	}

	public static boolean add(Integer key, xbean.UserDeviceInfo value) {
		return _Tables_.getInstance().userdeviceinfotab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().userdeviceinfotab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.UserDeviceInfo> getCache() {
		return _Tables_.getInstance().userdeviceinfotab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.UserDeviceInfo> getTable() {
		return _Tables_.getInstance().userdeviceinfotab;
	}

	public static xbean.UserDeviceInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, xbean.UserDeviceInfo>() {
			public xbean.UserDeviceInfo get(xbean.UserDeviceInfo v) { return v.toData(); }
		});
	}

	public static String selectTelcooper(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getTelcooper(); }
			});
	}

	public static String selectChid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getChid(); }
			});
	}

	public static String selectPlattype(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getPlattype(); }
			});
	}

	public static String selectDeviid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getDeviid(); }
			});
	}

	public static String selectImei(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getImei(); }
			});
	}

	public static String selectIp(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getIp(); }
			});
	}

	public static String selectNetenvir(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getNetenvir(); }
			});
	}

	public static String selectOsver(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getOsver(); }
			});
	}

	public static String selectBrtype(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getBrtype(); }
			});
	}

	public static String selectFirstdeviid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getFirstdeviid(); }
			});
	}

	public static String selectUsername(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserDeviceInfo, String>() {
				public String get(xbean.UserDeviceInfo v) { return v.getUsername(); }
			});
	}

}
