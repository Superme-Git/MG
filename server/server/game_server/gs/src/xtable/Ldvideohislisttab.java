package xtable;

// typed table access point
public class Ldvideohislisttab {
	Ldvideohislisttab() {
	}

	public static xbean.LDVideoHisList get(Integer key) {
		return _Tables_.getInstance().ldvideohislisttab.get(key);
	}

	public static xbean.LDVideoHisList get(Integer key, xbean.LDVideoHisList value) {
		return _Tables_.getInstance().ldvideohislisttab.get(key, value);
	}

	public static void insert(Integer key, xbean.LDVideoHisList value) {
		_Tables_.getInstance().ldvideohislisttab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().ldvideohislisttab.delete(key);
	}

	public static boolean add(Integer key, xbean.LDVideoHisList value) {
		return _Tables_.getInstance().ldvideohislisttab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().ldvideohislisttab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.LDVideoHisList> getCache() {
		return _Tables_.getInstance().ldvideohislisttab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.LDVideoHisList> getTable() {
		return _Tables_.getInstance().ldvideohislisttab;
	}

	public static xbean.LDVideoHisList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoHisList, xbean.LDVideoHisList>() {
			public xbean.LDVideoHisList get(xbean.LDVideoHisList v) { return v.toData(); }
		});
	}

	public static java.util.List<String> selectLdvidehisinfo(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoHisList, java.util.List<String>>() {
				public java.util.List<String> get(xbean.LDVideoHisList v) { return v.getLdvidehisinfoAsData(); }
			});
	}

}
