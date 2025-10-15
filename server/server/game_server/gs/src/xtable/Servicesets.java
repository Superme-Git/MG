package xtable;

// typed table access point
public class Servicesets {
	Servicesets() {
	}

	public static xbean.ServiceSet get(Integer key) {
		return _Tables_.getInstance().servicesets.get(key);
	}

	public static xbean.ServiceSet get(Integer key, xbean.ServiceSet value) {
		return _Tables_.getInstance().servicesets.get(key, value);
	}

	public static void insert(Integer key, xbean.ServiceSet value) {
		_Tables_.getInstance().servicesets.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().servicesets.delete(key);
	}

	public static boolean add(Integer key, xbean.ServiceSet value) {
		return _Tables_.getInstance().servicesets.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().servicesets.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ServiceSet> getCache() {
		return _Tables_.getInstance().servicesets.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ServiceSet> getTable() {
		return _Tables_.getInstance().servicesets;
	}

	public static xbean.ServiceSet select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceSet, xbean.ServiceSet>() {
			public xbean.ServiceSet get(xbean.ServiceSet v) { return v.toData(); }
		});
	}

	public static Integer selectBindtelagain(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceSet, Integer>() {
				public Integer get(xbean.ServiceSet v) { return v.getBindtelagain(); }
			});
	}

	public static Long selectBindtelagaintime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceSet, Long>() {
				public Long get(xbean.ServiceSet v) { return v.getBindtelagaintime(); }
			});
	}

}
