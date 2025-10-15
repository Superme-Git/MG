package xtable;

// typed table access point
public class Livedieweeknumtab {
	Livedieweeknumtab() {
	}

	public static xbean.LiveDieWeekNum get(Integer key) {
		return _Tables_.getInstance().livedieweeknumtab.get(key);
	}

	public static xbean.LiveDieWeekNum get(Integer key, xbean.LiveDieWeekNum value) {
		return _Tables_.getInstance().livedieweeknumtab.get(key, value);
	}

	public static void insert(Integer key, xbean.LiveDieWeekNum value) {
		_Tables_.getInstance().livedieweeknumtab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().livedieweeknumtab.delete(key);
	}

	public static boolean add(Integer key, xbean.LiveDieWeekNum value) {
		return _Tables_.getInstance().livedieweeknumtab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().livedieweeknumtab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.LiveDieWeekNum> getCache() {
		return _Tables_.getInstance().livedieweeknumtab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.LiveDieWeekNum> getTable() {
		return _Tables_.getInstance().livedieweeknumtab;
	}

	public static xbean.LiveDieWeekNum select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieWeekNum, xbean.LiveDieWeekNum>() {
			public xbean.LiveDieWeekNum get(xbean.LiveDieWeekNum v) { return v.toData(); }
		});
	}

	public static Long selectWeeknum(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieWeekNum, Long>() {
				public Long get(xbean.LiveDieWeekNum v) { return v.getWeeknum(); }
			});
	}

}
