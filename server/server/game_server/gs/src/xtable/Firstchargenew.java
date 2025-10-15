package xtable;

// typed table access point
public class Firstchargenew {
	Firstchargenew() {
	}

	public static xbean.FirstCharge get(Integer key) {
		return _Tables_.getInstance().firstchargenew.get(key);
	}

	public static xbean.FirstCharge get(Integer key, xbean.FirstCharge value) {
		return _Tables_.getInstance().firstchargenew.get(key, value);
	}

	public static void insert(Integer key, xbean.FirstCharge value) {
		_Tables_.getInstance().firstchargenew.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().firstchargenew.delete(key);
	}

	public static boolean add(Integer key, xbean.FirstCharge value) {
		return _Tables_.getInstance().firstchargenew.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().firstchargenew.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.FirstCharge> getCache() {
		return _Tables_.getInstance().firstchargenew.getCache();
	}

	public static mkdb.TTable<Integer, xbean.FirstCharge> getTable() {
		return _Tables_.getInstance().firstchargenew;
	}

	public static xbean.FirstCharge select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FirstCharge, xbean.FirstCharge>() {
			public xbean.FirstCharge get(xbean.FirstCharge v) { return v.toData(); }
		});
	}

	public static Long selectFirstchargetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FirstCharge, Long>() {
				public Long get(xbean.FirstCharge v) { return v.getFirstchargetime(); }
			});
	}

	public static Long selectFirstchargeclearpresenttime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FirstCharge, Long>() {
				public Long get(xbean.FirstCharge v) { return v.getFirstchargeclearpresenttime(); }
			});
	}

	public static Long selectChargestatus(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FirstCharge, Long>() {
				public Long get(xbean.FirstCharge v) { return v.getChargestatus(); }
			});
	}

}
