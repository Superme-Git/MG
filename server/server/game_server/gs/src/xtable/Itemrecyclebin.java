package xtable;

// typed table access point
public class Itemrecyclebin {
	Itemrecyclebin() {
	}

	public static xbean.DiscardItem get(Long key) {
		return _Tables_.getInstance().itemrecyclebin.get(key);
	}

	public static xbean.DiscardItem get(Long key, xbean.DiscardItem value) {
		return _Tables_.getInstance().itemrecyclebin.get(key, value);
	}

	public static void insert(Long key, xbean.DiscardItem value) {
		_Tables_.getInstance().itemrecyclebin.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().itemrecyclebin.delete(key);
	}

	public static boolean add(Long key, xbean.DiscardItem value) {
		return _Tables_.getInstance().itemrecyclebin.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().itemrecyclebin.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.DiscardItem> getCache() {
		return _Tables_.getInstance().itemrecyclebin.getCache();
	}

	public static mkdb.TTable<Long, xbean.DiscardItem> getTable() {
		return _Tables_.getInstance().itemrecyclebin;
	}

	public static xbean.DiscardItem select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardItem, xbean.DiscardItem>() {
			public xbean.DiscardItem get(xbean.DiscardItem v) { return v.toData(); }
		});
	}

	public static xbean.Item selectItem(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardItem, xbean.Item>() {
				public xbean.Item get(xbean.DiscardItem v) { return v.getItem(); }
			});
	}

	public static Long selectDeletedate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardItem, Long>() {
				public Long get(xbean.DiscardItem v) { return v.getDeletedate(); }
			});
	}

}
