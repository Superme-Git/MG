package xtable;

// typed table access point
public class Fushinum {
	Fushinum() {
	}

	public static xbean.YbNums get(Integer key) {
		return _Tables_.getInstance().fushinum.get(key);
	}

	public static xbean.YbNums get(Integer key, xbean.YbNums value) {
		return _Tables_.getInstance().fushinum.get(key, value);
	}

	public static void insert(Integer key, xbean.YbNums value) {
		_Tables_.getInstance().fushinum.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().fushinum.delete(key);
	}

	public static boolean add(Integer key, xbean.YbNums value) {
		return _Tables_.getInstance().fushinum.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().fushinum.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.YbNums> getCache() {
		return _Tables_.getInstance().fushinum.getCache();
	}

	public static mkdb.TTable<Integer, xbean.YbNums> getTable() {
		return _Tables_.getInstance().fushinum;
	}

	public static xbean.YbNums select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YbNums, xbean.YbNums>() {
			public xbean.YbNums get(xbean.YbNums v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.YbNum> selectRoleyb(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YbNums, java.util.Map<Long, xbean.YbNum>>() {
				public java.util.Map<Long, xbean.YbNum> get(xbean.YbNums v) { return v.getRoleybAsData(); }
			});
	}

}
