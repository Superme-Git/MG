package xtable;

// typed table access point
public class Regtbl {
	Regtbl() {
	}

	public static xbean.RegRec get(Long key) {
		return _Tables_.getInstance().regtbl.get(key);
	}

	public static xbean.RegRec get(Long key, xbean.RegRec value) {
		return _Tables_.getInstance().regtbl.get(key, value);
	}

	public static void insert(Long key, xbean.RegRec value) {
		_Tables_.getInstance().regtbl.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().regtbl.delete(key);
	}

	public static boolean add(Long key, xbean.RegRec value) {
		return _Tables_.getInstance().regtbl.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().regtbl.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RegRec> getCache() {
		return _Tables_.getInstance().regtbl.getCache();
	}

	public static mkdb.TTable<Long, xbean.RegRec> getTable() {
		return _Tables_.getInstance().regtbl;
	}

	public static xbean.RegRec select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RegRec, xbean.RegRec>() {
			public xbean.RegRec get(xbean.RegRec v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.RegMonth> selectMonthmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RegRec, java.util.Map<Integer, xbean.RegMonth>>() {
				public java.util.Map<Integer, xbean.RegMonth> get(xbean.RegRec v) { return v.getMonthmapAsData(); }
			});
	}

	public static Long selectLastregtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RegRec, Long>() {
				public Long get(xbean.RegRec v) { return v.getLastregtime(); }
			});
	}

}
