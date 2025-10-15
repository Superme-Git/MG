package xtable;

// typed table access point
public class Yingyongbaoinfos {
	Yingyongbaoinfos() {
	}

	public static xbean.YingYongBao get(Integer key) {
		return _Tables_.getInstance().yingyongbaoinfos.get(key);
	}

	public static xbean.YingYongBao get(Integer key, xbean.YingYongBao value) {
		return _Tables_.getInstance().yingyongbaoinfos.get(key, value);
	}

	public static void insert(Integer key, xbean.YingYongBao value) {
		_Tables_.getInstance().yingyongbaoinfos.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().yingyongbaoinfos.delete(key);
	}

	public static boolean add(Integer key, xbean.YingYongBao value) {
		return _Tables_.getInstance().yingyongbaoinfos.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().yingyongbaoinfos.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.YingYongBao> getCache() {
		return _Tables_.getInstance().yingyongbaoinfos.getCache();
	}

	public static mkdb.TTable<Integer, xbean.YingYongBao> getTable() {
		return _Tables_.getInstance().yingyongbaoinfos;
	}

	public static xbean.YingYongBao select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, xbean.YingYongBao>() {
			public xbean.YingYongBao get(xbean.YingYongBao v) { return v.toData(); }
		});
	}

	public static String selectOpenid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getOpenid(); }
			});
	}

	public static String selectOpenkey(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getOpenkey(); }
			});
	}

	public static String selectPaytoken(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getPaytoken(); }
			});
	}

	public static String selectPf(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getPf(); }
			});
	}

	public static String selectPfkey(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getPfkey(); }
			});
	}

	public static String selectZoneid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getZoneid(); }
			});
	}

	public static String selectPlatformname(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YingYongBao, String>() {
				public String get(xbean.YingYongBao v) { return v.getPlatformname(); }
			});
	}

}
