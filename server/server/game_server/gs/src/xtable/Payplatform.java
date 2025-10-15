package xtable;

// typed table access point
public class Payplatform {
	Payplatform() {
	}

	public static xbean.PlatformInfo get(Integer key) {
		return _Tables_.getInstance().payplatform.get(key);
	}

	public static xbean.PlatformInfo get(Integer key, xbean.PlatformInfo value) {
		return _Tables_.getInstance().payplatform.get(key, value);
	}

	public static void insert(Integer key, xbean.PlatformInfo value) {
		_Tables_.getInstance().payplatform.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().payplatform.delete(key);
	}

	public static boolean add(Integer key, xbean.PlatformInfo value) {
		return _Tables_.getInstance().payplatform.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().payplatform.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PlatformInfo> getCache() {
		return _Tables_.getInstance().payplatform.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PlatformInfo> getTable() {
		return _Tables_.getInstance().payplatform;
	}

	public static xbean.PlatformInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PlatformInfo, xbean.PlatformInfo>() {
			public xbean.PlatformInfo get(xbean.PlatformInfo v) { return v.toData(); }
		});
	}

	public static Integer selectId(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PlatformInfo, Integer>() {
				public Integer get(xbean.PlatformInfo v) { return v.getId(); }
			});
	}

	public static String selectName(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PlatformInfo, String>() {
				public String get(xbean.PlatformInfo v) { return v.getName(); }
			});
	}

	public static Integer selectDiscount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PlatformInfo, Integer>() {
				public Integer get(xbean.PlatformInfo v) { return v.getDiscount(); }
			});
	}

}
