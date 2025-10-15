package xtable;

// typed table access point
public class Randomnametab {
	Randomnametab() {
	}

	public static xbean.RandomNameInfo get(Integer key) {
		return _Tables_.getInstance().randomnametab.get(key);
	}

	public static xbean.RandomNameInfo get(Integer key, xbean.RandomNameInfo value) {
		return _Tables_.getInstance().randomnametab.get(key, value);
	}

	public static void insert(Integer key, xbean.RandomNameInfo value) {
		_Tables_.getInstance().randomnametab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().randomnametab.delete(key);
	}

	public static boolean add(Integer key, xbean.RandomNameInfo value) {
		return _Tables_.getInstance().randomnametab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().randomnametab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RandomNameInfo> getCache() {
		return _Tables_.getInstance().randomnametab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RandomNameInfo> getTable() {
		return _Tables_.getInstance().randomnametab;
	}

	public static xbean.RandomNameInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RandomNameInfo, xbean.RandomNameInfo>() {
			public xbean.RandomNameInfo get(xbean.RandomNameInfo v) { return v.toData(); }
		});
	}

	public static Long selectRandomtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RandomNameInfo, Long>() {
				public Long get(xbean.RandomNameInfo v) { return v.getRandomtime(); }
			});
	}

}
