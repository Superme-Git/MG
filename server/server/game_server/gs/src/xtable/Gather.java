package xtable;

// typed table access point
public class Gather {
	Gather() {
	}

	public static xbean.Gather get(Long key) {
		return _Tables_.getInstance().gather.get(key);
	}

	public static xbean.Gather get(Long key, xbean.Gather value) {
		return _Tables_.getInstance().gather.get(key, value);
	}

	public static void insert(Long key, xbean.Gather value) {
		_Tables_.getInstance().gather.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().gather.delete(key);
	}

	public static boolean add(Long key, xbean.Gather value) {
		return _Tables_.getInstance().gather.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().gather.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Gather> getCache() {
		return _Tables_.getInstance().gather.getCache();
	}

	public static mkdb.TTable<Long, xbean.Gather> getTable() {
		return _Tables_.getInstance().gather;
	}

	public static xbean.Gather select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Gather, xbean.Gather>() {
			public xbean.Gather get(xbean.Gather v) { return v.toData(); }
		});
	}

	public static Long selectGatherkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Gather, Long>() {
				public Long get(xbean.Gather v) { return v.getGatherkey(); }
			});
	}

	public static Integer selectResult(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Gather, Integer>() {
				public Integer get(xbean.Gather v) { return v.getResult(); }
			});
	}

	public static Long selectEndgathertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Gather, Long>() {
				public Long get(xbean.Gather v) { return v.getEndgathertime(); }
			});
	}

}
