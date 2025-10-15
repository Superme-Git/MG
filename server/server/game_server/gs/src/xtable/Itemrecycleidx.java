package xtable;

// typed table access point
public class Itemrecycleidx {
	Itemrecycleidx() {
	}

	public static xbean.ItemRecycleIndex get(xbean.ItemRecycleDate key) {
		return _Tables_.getInstance().itemrecycleidx.get(key);
	}

	public static xbean.ItemRecycleIndex get(xbean.ItemRecycleDate key, xbean.ItemRecycleIndex value) {
		return _Tables_.getInstance().itemrecycleidx.get(key, value);
	}

	public static void insert(xbean.ItemRecycleDate key, xbean.ItemRecycleIndex value) {
		_Tables_.getInstance().itemrecycleidx.insert(key, value);
	}

	public static void delete(xbean.ItemRecycleDate key) {
		_Tables_.getInstance().itemrecycleidx.delete(key);
	}

	public static boolean add(xbean.ItemRecycleDate key, xbean.ItemRecycleIndex value) {
		return _Tables_.getInstance().itemrecycleidx.add(key, value);
	}

	public static boolean remove(xbean.ItemRecycleDate key) {
		return _Tables_.getInstance().itemrecycleidx.remove(key);
	}

	public static mkdb.TTableCache<xbean.ItemRecycleDate, xbean.ItemRecycleIndex> getCache() {
		return _Tables_.getInstance().itemrecycleidx.getCache();
	}

	public static mkdb.TTable<xbean.ItemRecycleDate, xbean.ItemRecycleIndex> getTable() {
		return _Tables_.getInstance().itemrecycleidx;
	}

	public static xbean.ItemRecycleIndex select(xbean.ItemRecycleDate key) {
		return getTable().select(key, new mkdb.TField<xbean.ItemRecycleIndex, xbean.ItemRecycleIndex>() {
			public xbean.ItemRecycleIndex get(xbean.ItemRecycleIndex v) { return v.toData(); }
		});
	}

	public static java.util.Set<Long> selectDayrecycle(xbean.ItemRecycleDate key) {
		return getTable().select(key, new mkdb.TField<xbean.ItemRecycleIndex, java.util.Set<Long>>() {
				public java.util.Set<Long> get(xbean.ItemRecycleIndex v) { return v.getDayrecycleAsData(); }
			});
	}

}
