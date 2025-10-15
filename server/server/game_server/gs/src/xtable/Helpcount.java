package xtable;

// typed table access point
public class Helpcount {
	Helpcount() {
	}

	public static xbean.helpcount get(Long key) {
		return _Tables_.getInstance().helpcount.get(key);
	}

	public static xbean.helpcount get(Long key, xbean.helpcount value) {
		return _Tables_.getInstance().helpcount.get(key, value);
	}

	public static void insert(Long key, xbean.helpcount value) {
		_Tables_.getInstance().helpcount.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().helpcount.delete(key);
	}

	public static boolean add(Long key, xbean.helpcount value) {
		return _Tables_.getInstance().helpcount.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().helpcount.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.helpcount> getCache() {
		return _Tables_.getInstance().helpcount.getCache();
	}

	public static mkdb.TTable<Long, xbean.helpcount> getTable() {
		return _Tables_.getInstance().helpcount;
	}

	public static xbean.helpcount select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, xbean.helpcount>() {
			public xbean.helpcount get(xbean.helpcount v) { return v.toData(); }
		});
	}

	public static Long selectExpvalue(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Long>() {
				public Long get(xbean.helpcount v) { return v.getExpvalue(); }
			});
	}

	public static Integer selectShengwangvalue(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Integer>() {
				public Integer get(xbean.helpcount v) { return v.getShengwangvalue(); }
			});
	}

	public static Integer selectClanvalue(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Integer>() {
				public Integer get(xbean.helpcount v) { return v.getClanvalue(); }
			});
	}

	public static Integer selectClanhishelpnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Integer>() {
				public Integer get(xbean.helpcount v) { return v.getClanhishelpnum(); }
			});
	}

	public static Integer selectHelpgiveitemnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Integer>() {
				public Integer get(xbean.helpcount v) { return v.getHelpgiveitemnum(); }
			});
	}

	public static Integer selectHelpitemnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.helpcount, Integer>() {
				public Integer get(xbean.helpcount v) { return v.getHelpitemnum(); }
			});
	}

}
