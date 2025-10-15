package xtable;

// typed table access point
public class Weekindexs {
	Weekindexs() {
	}

	public static xbean.WeekIndex get(Integer key) {
		return _Tables_.getInstance().weekindexs.get(key);
	}

	public static xbean.WeekIndex get(Integer key, xbean.WeekIndex value) {
		return _Tables_.getInstance().weekindexs.get(key, value);
	}

	public static void insert(Integer key, xbean.WeekIndex value) {
		_Tables_.getInstance().weekindexs.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().weekindexs.delete(key);
	}

	public static boolean add(Integer key, xbean.WeekIndex value) {
		return _Tables_.getInstance().weekindexs.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().weekindexs.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.WeekIndex> getCache() {
		return _Tables_.getInstance().weekindexs.getCache();
	}

	public static mkdb.TTable<Integer, xbean.WeekIndex> getTable() {
		return _Tables_.getInstance().weekindexs;
	}

	public static xbean.WeekIndex select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.WeekIndex, xbean.WeekIndex>() {
			public xbean.WeekIndex get(xbean.WeekIndex v) { return v.toData(); }
		});
	}

	public static Integer selectIndex(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.WeekIndex, Integer>() {
				public Integer get(xbean.WeekIndex v) { return v.getIndex(); }
			});
	}

	public static Integer selectInstanceindex(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.WeekIndex, Integer>() {
				public Integer get(xbean.WeekIndex v) { return v.getInstanceindex(); }
			});
	}

}
