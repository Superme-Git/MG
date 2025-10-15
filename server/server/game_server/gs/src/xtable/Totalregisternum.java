package xtable;

// typed table access point
public class Totalregisternum {
	Totalregisternum() {
	}

	public static xbean.totalNumber get(Integer key) {
		return _Tables_.getInstance().totalregisternum.get(key);
	}

	public static xbean.totalNumber get(Integer key, xbean.totalNumber value) {
		return _Tables_.getInstance().totalregisternum.get(key, value);
	}

	public static void insert(Integer key, xbean.totalNumber value) {
		_Tables_.getInstance().totalregisternum.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().totalregisternum.delete(key);
	}

	public static boolean add(Integer key, xbean.totalNumber value) {
		return _Tables_.getInstance().totalregisternum.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().totalregisternum.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.totalNumber> getCache() {
		return _Tables_.getInstance().totalregisternum.getCache();
	}

	public static mkdb.TTable<Integer, xbean.totalNumber> getTable() {
		return _Tables_.getInstance().totalregisternum;
	}

	public static xbean.totalNumber select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.totalNumber, xbean.totalNumber>() {
			public xbean.totalNumber get(xbean.totalNumber v) { return v.toData(); }
		});
	}

	public static Long selectTotalnumber(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.totalNumber, Long>() {
				public Long get(xbean.totalNumber v) { return v.getTotalnumber(); }
			});
	}

}
