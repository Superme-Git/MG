package xtable;

// typed table access point
public class Yybfushi {
	Yybfushi() {
	}

	public static xbean.YybFushiNums get(Integer key) {
		return _Tables_.getInstance().yybfushi.get(key);
	}

	public static xbean.YybFushiNums get(Integer key, xbean.YybFushiNums value) {
		return _Tables_.getInstance().yybfushi.get(key, value);
	}

	public static void insert(Integer key, xbean.YybFushiNums value) {
		_Tables_.getInstance().yybfushi.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().yybfushi.delete(key);
	}

	public static boolean add(Integer key, xbean.YybFushiNums value) {
		return _Tables_.getInstance().yybfushi.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().yybfushi.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.YybFushiNums> getCache() {
		return _Tables_.getInstance().yybfushi.getCache();
	}

	public static mkdb.TTable<Integer, xbean.YybFushiNums> getTable() {
		return _Tables_.getInstance().yybfushi;
	}

	public static xbean.YybFushiNums select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YybFushiNums, xbean.YybFushiNums>() {
			public xbean.YybFushiNums get(xbean.YybFushiNums v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.YybFushiNum> selectRolefushi(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.YybFushiNums, java.util.Map<Long, xbean.YybFushiNum>>() {
				public java.util.Map<Long, xbean.YybFushiNum> get(xbean.YybFushiNums v) { return v.getRolefushiAsData(); }
			});
	}

}
